package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import controller.HttpResponseMapper;
import controller.RequestController;
import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader((new InputStreamReader(in, StandardCharsets.UTF_8)));
            DataOutputStream dos = new DataOutputStream(out);

            HttpRequest httpRequest = new HttpRequest(br);
            HttpResponse httpResponse = RequestController.getResponse(httpRequest);

            sendResponse(dos, httpResponse);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendResponse(DataOutputStream dos, HttpResponse httpResponse){
        switch (httpResponse.getStatusLine().getStatus()){
            case OK :{
                HttpResponseMapper.response200Header(dos, httpResponse);
                HttpResponseMapper.responseBody(dos, httpResponse.getBody());
            }
        }
    }
}
