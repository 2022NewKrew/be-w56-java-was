package webserver;

import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

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
             OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = new HttpRequest();

            // RequestLine 처리
            String requestLine = br.readLine();
            httpRequest.setRequestLine(requestLine);

            // Header 처리
            String headerSingleline = " ";
            while (true) {
                headerSingleline = br.readLine();
                if (headerSingleline == null || headerSingleline.equals("")) {
                    break;
                }
                httpRequest.setHeaderValue(headerSingleline);
            }

            // Response 처리
            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setBody(httpRequest.getRequestLine().getUrl());
            httpResponse.response200Header(dos, httpResponse.getBody().length, "");
            httpResponse.responseBody(dos, httpResponse.getBody());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }



}
