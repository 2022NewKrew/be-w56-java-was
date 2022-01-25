package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import db.DataBase;
import lombok.Synchronized;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);
            handleRequest(br, dos);
        } catch (IOException | InvalidHttpRequestException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/html; charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

    private void response302Header(DataOutputStream dos, int lengthOfBodyContent, String url) throws IOException {
        dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
        dos.writeBytes("Location: " + url + "\r\n");
        dos.writeBytes("Content-Type: text/html; charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
        dos.flush();
    }

    private void response404Header(DataOutputStream dos) throws IOException {
        dos.writeBytes("HTTP/1.1 404 NOT FOUND \r\n");
        dos.writeBytes("\r\n");
        dos.flush();
    }

    private void handleRequest(BufferedReader br, DataOutputStream dos) throws IOException, InvalidHttpRequestException {
        Map<String, String> startLine = parseStartLine(br);
        Map<String, String> header = parseHeader(br);

        switch (startLine.get("method")) {
            case "GET":
                handleGetRequest(dos, startLine.get("url"), header);
                break;

            case "POST":
                handlePostRequest(br, dos, startLine.get("url"), header);
                break;

            default:
                throw new InvalidHttpRequestException("잘못된 HTTP method");
        }
    }

    private Map<String, String> parseStartLine(BufferedReader br) throws IOException, InvalidHttpRequestException {
        Map<String, String> startLine = new HashMap<>();
        String line = br.readLine();
        log.debug(line);

        try {
            String[] tokens = line.split(" ");
            startLine.put("method", tokens[0]);
            startLine.put("url", tokens[1].equals("/") ? "/index.html" : tokens[1]);
            startLine.put("version", tokens[2]);
            return startLine;
        } catch (NullPointerException e) {
            throw new InvalidHttpRequestException("빈 HTTP 요청", e);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidHttpRequestException("잘못된 HTTP 요청 시작 줄", e);
        }
    }

    private Map<String, String> parseHeader(BufferedReader br) throws IOException {
        Map<String, String> header = new HashMap<>();
        StringBuilder headerLog = new StringBuilder("\n");

        String line = br.readLine();
        while (!"".equals(line)) {
            headerLog.append(line).append('\n');
            HttpRequestUtils.Pair keyValues = HttpRequestUtils.parseHeader(line);
            if (keyValues != null) {
                header.put(keyValues.getKey(), keyValues.getValue());
            }
            line = br.readLine();
        }

        log.debug(headerLog.toString());
        return header;
    }

    private void handleGetRequest(DataOutputStream dos, String url, Map<String, String> header) throws IOException {
        try {
            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (FileNotFoundException e) {
            response404Header(dos);
        }
    }

    private void handlePostRequest(BufferedReader br, DataOutputStream dos, String url, Map<String, String> header) throws IOException {
        try {
            int contentLength = Integer.parseInt(header.get("Content-Length"));
            switch (url) {
                case "/user":
                    handleUserPostRequest(br, dos, contentLength);

                default:
                    response404Header(dos);
            }
        } catch (NoSuchElementException e) {
            log.debug("Content-Length 필드가 없음: {}", e.getMessage());
        } catch (NumberFormatException e) {
            log.debug("Content-Length 필드가 잘못됨: {}", e.getMessage());
        } catch (InvalidPostBodyException e) {
            log.debug(e.getMessage());
        }
    }

    @Synchronized
    private void handleUserPostRequest(BufferedReader br, DataOutputStream dos, int contentLength)
            throws IOException, InvalidPostBodyException {
        String body = IOUtils.readData(br, contentLength);
        Map<String, String> parseQueryString = HttpRequestUtils.parseQueryString(body);
        try {
            DataBase.addUser(new User(parseQueryString.get("userId"),
                    parseQueryString.get("password"),
                    parseQueryString.get("name"),
                    parseQueryString.get("email")));
            response302Header(dos, 0, "/index.html");
        } catch (NoSuchElementException e) {
            throw new InvalidPostBodyException("/user 잘못된 body 형식", e);
        }
    }
}
