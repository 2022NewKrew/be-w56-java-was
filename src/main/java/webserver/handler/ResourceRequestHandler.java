package webserver.handler;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.HttpRequest;

public class ResourceRequestHandler extends Handler {
    private static final Logger log = LoggerFactory.getLogger(ResourceRequestHandler.class);

    private Socket connection;
    private HttpRequest httpRequest;

    private String method = "GET";
    private String path = "/index.html";

    public ResourceRequestHandler() {}

    public ResourceRequestHandler(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
        this.connection = httpRequest.getConnection();
    }

    @Override
    public ResourceRequestHandler getHandlerWithRequest(HttpRequest httpRequest) {
        return new ResourceRequestHandler(httpRequest);
    }

//    @Override
//    public String handle(HttpRequest httpRequest) {
//        String viewName;
//        this.start();
//    }

    @Override
    public void handle(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
        this.connection = httpRequest.getConnection();

        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + httpRequest.getPath()).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + getMediaType(httpRequest.getPath()) + "\r\n");
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

    @Override
    public boolean isSupport(HttpRequest httpRequest) {
        System.out.println("method: " + this.method + ", " + httpRequest.getMethod());
        System.out.println("path: " + this.path + ", " + httpRequest.getPath());
//        return this.method.equals(httpRequest.getMethod()) && this.path.equals(httpRequest.getPath());
        return true;
    }

    private String getMediaType(String file) {
        if (file == null) {
            return null;
        }
        int period = file.lastIndexOf('.');
        if (period < 0) {
            return null;
        }
        String extension = file.substring(period + 1);
        if (extension.length() < 1) {
            return null;
        }
        return HttpRequestUtils.findMimeMapping(extension);
    }
}
