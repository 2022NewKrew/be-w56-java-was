package webserver;

import exception.CustomException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.Handler;
import webserver.handler.HandlerFactory;
import webserver.request.Request;
import webserver.response.Response;
import webserver.response.StatusCode;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private Request request;
    private Response response;
    private Handler handler;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            request = Request.create(in);
            response = Response.create(out);

            handler = HandlerFactory.createHandler(request);
            handler.handle(request, response);



            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            //DataOutputStream dos = new DataOutputStream(out);
            //byte[] body = "Hello World".getBytes();
            //response200Header(dos, body.length);
            //responseBody(dos, body);
        } catch (CustomException e) {
            response = Response.createErrorResponse(connection, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            response = Response.createErrorResponse(connection, StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            response.write();
        }
    }
/*
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
    }*/
}
