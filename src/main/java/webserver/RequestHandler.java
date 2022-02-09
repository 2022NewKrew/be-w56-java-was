package webserver;

import Controller.HttpController;
import db.DataBase;
import http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DynamicHtmlBuilder;

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
            // Request 파싱
            HttpRequestParser httpRequestParser = new HttpRequestParser();
            httpRequestParser.parse(in);
            HttpRequest httpRequest = httpRequestParser.getHttpRequest();
            // Request 확인을 위한 logging
            log.debug(httpRequest.toString());

            // Request 처리
            HttpController httpController = new HttpController(httpRequest);
            String path = httpController.runServiceAndReturnPath();

            // Response 객체 build
            HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder(httpRequest);
            httpResponseBuilder.build(path);
            HttpResponse httpResponse = httpResponseBuilder.getHttpResponse();

            // Response 전송
            HttpResponseSender httpResponseSender = new HttpResponseSender(httpResponse, out);
            httpResponseSender.sendResponse();

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
