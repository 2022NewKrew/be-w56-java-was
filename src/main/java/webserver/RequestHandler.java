package webserver;

import http.HttpStatus;
import http.request.HttpRequestLine;
import http.response.HttpResponseBody;
import http.response.HttpResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream();
             InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr);
             DataOutputStream dos = new DataOutputStream(out);) {

            String line = br.readLine();
            HttpRequestLine requestLine = HttpRequestLine.parseRequestLine(line);

            while (!"".equals(line)) {
                log.debug(line);

                line = br.readLine();
            }

            HttpResponseBody responseBody = HttpResponseBody.createFromUrl(requestLine.url());
            HttpResponseHeader responseHeader = new HttpResponseHeader(requestLine.url(), HttpStatus.OK, responseBody.length());

            responseHeader.writeToDataOutputStream(dos);
            responseBody.writeToDataOutputStream(dos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
