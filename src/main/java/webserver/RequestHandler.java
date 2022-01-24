package webserver;

import model.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.List;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final byte[] NOT_FOUNT_MESSAGE = "없는 페이지 입니다.".getBytes();

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = HttpRequestUtils.createRequest(in);
            printHeaders(request.getHeader());
            DataOutputStream dos = new DataOutputStream(out);
            File file = new File("./webapp" + request.getPath());
            byte[] body;
            if (file.exists()) {
                body = Files.readAllBytes(file.toPath());
                response200Header(dos, body.length);
            } else {
                body = NOT_FOUNT_MESSAGE;
                response404Header(dos);
            }
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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

    private void response404Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + NOT_FOUNT_MESSAGE.length + "\r\n");
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

    private void printHeaders(List<Pair> header) {
        for (Pair pair : header) {
            log.info("Header : [{} : {}]", pair.getKey(), pair.getValue());
        }
    }
}
