package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpStatusCode;
import util.ParseRequest;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;


public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final Controller controller;
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.controller = Controller.INSTANCE;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            byte[] body = new byte[0];
            DataOutputStream dos = new DataOutputStream(out);
            ParseRequest parseRequest = new ParseRequest(br);
            Map<String, String> requestInfo = parseRequest.getRequestMap();

            if (requestInfo.get("method").equals("GET")) {
                body = controller.getRequest(requestInfo);
                responseHeader(dos, "OK", body.length, requestInfo);
            }
            if (requestInfo.get("method").equals("POST")) {
                requestInfo.putAll(controller.postRequest(requestInfo));
                responseHeader(dos, "FOUND", body.length, requestInfo);
            }
            responseBody(dos, body);

        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void responseHeader(DataOutputStream dos, String statusCode, int lengthOfBodyContent, Map<String, String> additionalInfo) {
        String contentTypeMessage = "Content-Type: " + additionalInfo.get("Accept").split(",")[0] + ";charset=utf-8\r\n";
        try {
            dos.writeBytes("HTTP/1.1 " + HttpStatusCode.valueOf(statusCode).getStatusCode() + " " + statusCode + "\r\n");
            dos.writeBytes(contentTypeMessage);
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            if (statusCode.equals("FOUND")) {
                dos.writeBytes("Location: " + additionalInfo.get("location") + "\r\n");
                dos.writeBytes("Set-Cookie: " + additionalInfo.get("Cookie") + "\r\n");
            }
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
