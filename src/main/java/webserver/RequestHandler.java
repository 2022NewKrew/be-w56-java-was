package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import http.HttpHeaders;
import http.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort()); //

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in)); DataOutputStream dos = new DataOutputStream(out)) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = getHttpRequest(br);
            byte[] body = Files.readAllBytes(new File("./webapp" + httpRequest.getUrl()).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private HttpRequest getHttpRequest(BufferedReader br) throws IOException {
        String line = "";
        if ((line = br.readLine()) == null) {
            throw new IllegalArgumentException("invalid http request");
        }
        String requestLine = line;

        List<String> requestHeader = new ArrayList<>();
        while (line != null && !line.equals("")) {
            log.info("header = {}", line);
            line = br.readLine();
            requestHeader.add(line);
        }

        List<String> requestBody = new ArrayList<>();
        while (line != null && !line.equals("")) {
            log.info("body = {}", line);
            line = br.readLine();
            requestBody.add(line);
        }
        return new HttpRequest(requestLine, requestHeader, requestBody);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
//            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
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
}
