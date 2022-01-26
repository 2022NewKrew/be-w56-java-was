package webserver;

import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequestParser httpRequestParser = new HttpRequestParser();
            httpRequestParser.parse(in);
            HttpRequest httpRequest = httpRequestParser.getHttpRequest();

            // Response 처리
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setDos(out);
            httpResponse.setBody(httpRequest.getUrl());
            httpResponse.response200Header(httpRequest.getUrl());
            httpResponse.responseBody(httpResponse.getBody());

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
