package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import db.DataBase;
import exception.InvalidHttpMethodException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import javax.xml.crypto.Data;

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
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);
            String line = br.readLine();
            //log.debug("request line : {}", line);
            Map<String, String> requestLine = HttpRequestUtils.parseRequestLine(line);
            Map<String, String> header = HttpRequestUtils.parseHeader(br);
            RequestMapper(requestLine, header, dos, br);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    private void RequestMapper(Map<String, String> requestLine, Map<String, String> header, DataOutputStream dos, BufferedReader br) throws InvalidHttpMethodException, IOException {
        String url = requestLine.get("url");
        switch (requestLine.get("method")) {
            case "GET":
                if (url.contains("?")) {
                    int index = url.indexOf("?");
                    String requestPath = url.substring(0, index);
                    String queryString = url.substring(index + 1);
                    Map<String, String> params = HttpRequestUtils.parseQueryString(queryString);
                    urlMapper(dos, requestPath, header, params);
                    break;
                }
                urlMapper(dos, url, header, br);
                break;
            case "POST":
                urlMapper(dos, url, header, br);
                break;
            default:
                throw new InvalidHttpMethodException("유효하지 않은 HTTP 메소드입니다");
        }
    }

    private void urlMapper(DataOutputStream dos, String url, Map<String, String> header, BufferedReader br) throws IOException {
        if (url.contains(".html")) {
            getHtml(dos, url, header);
        }
        if (url.startsWith("/create")) {
            createUserByPost(dos, url, header, br);
        }
    }

    private void urlMapper(DataOutputStream dos, String url, Map<String, String> header, Map<String, String> params) throws IOException {
        if (url.startsWith("/create")) {
            createUserByGet(dos, url, header, params);
        }
    }

    private void getHtml(DataOutputStream dos, String url, Map<String, String> header) throws IOException {
        view(dos, url, header);
    }

    private void createUserByGet(DataOutputStream dos, String url, Map<String, String> header, Map<String, String> params) throws IOException {
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        System.out.println(user);
        DataBase.addUser(user);
        view(dos, "/index.html", header);
    }

    private void createUserByPost(DataOutputStream dos, String url, Map<String, String> header, BufferedReader br) throws IOException {
        String body = IOUtils.readData(br,Integer.parseInt(header.get("Content-Length")));
        Map<String, String> params = HttpRequestUtils.parseQueryString(body);
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        System.out.println(user);
        DataBase.addUser(user);
        view(dos, "/index.html", header);
    }


    private void view(DataOutputStream dos, String url, Map<String, String> header) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        response200Header(dos, body.length);
        responseBody(dos, body);
    }


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void show(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

}
