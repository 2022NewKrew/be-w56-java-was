package com.kakao.webserver;

import com.kakao.http.header.ContentLengthHeader;
import com.kakao.http.header.ContentTypeHeader;
import com.kakao.http.header.HttpHeader;
import com.kakao.http.request.HttpRequest;
import com.kakao.http.response.HttpResponse;
import com.kakao.http.response.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String DEFAULT_HTTP_VERSION = "HTTP/1.1";

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(in);
            Path targetPath = buildProperPath(httpRequest.getUrl().getPath());
            responseFile(out, targetPath.toFile());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Path buildProperPath(String path) {
        if (path.equals("/")) {
            return Path.of("./webapp/index.html");
        }
        return Path.of("./webapp", path);
    }

    private void responseFile(OutputStream out, File file) {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            List<HttpHeader> headers = List.of(new ContentTypeHeader(file.getName()),
                    new ContentLengthHeader(file.length()));
            HttpResponse httpResponse = new HttpResponse(DEFAULT_HTTP_VERSION, HttpStatus.OK, headers);
            dos.writeBytes(httpResponse.toString());
            responseBody(dos, Files.readAllBytes(file.toPath()));
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
