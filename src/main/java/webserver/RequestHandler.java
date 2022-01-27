package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import http.HttpHeader;
import http.request.HttpRequest;
import http.request.HttpRequestStartLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = HttpRequest.create(in);
            printRequest(request);

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + request.getStartLine().getTargetUri()).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    private void printRequest(HttpRequest httpRequest) {
        printHttpRequestStartLine(httpRequest.getStartLine());
        printHttpRequestHeader(httpRequest.getHeader());
    }

    private void printHttpRequestStartLine(HttpRequestStartLine startLine) {
        log.debug("(STARTLINE)method : {}", startLine.getMethod());
        log.debug("(STARTLINE)targetUri : {}", startLine.getTargetUri());
        log.debug("(STARTLINE)http version : {}", startLine.getHttpVersion());
    }

    private void printHttpRequestHeader(HttpHeader header) {
        Map<String, String> headers = header.getHeaders();
        for (Map.Entry<String, String> entrySet : headers.entrySet()) {
            log.debug("(HEADER) {} : {}", entrySet.getKey(), entrySet.getValue());
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
