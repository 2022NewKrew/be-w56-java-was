package webserver.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseHeader;
import webserver.response.HttpResponseStatusLine;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final WebService webService;
    private final RequestParser requestParser;

    public RequestHandler(Socket connectionSocket, WebService webService, RequestParser requestParser) {
        this.connection = connectionSocket;
        this.webService = webService;
        this.requestParser = requestParser;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = requestParser.doParse(in);

            HttpResponse httpResponse = new HttpResponse(new DataOutputStream(out));
            httpResponse.setHttpResponseStatusLine(new HttpResponseStatusLine());
            httpResponse.setHttpResponseHeader(new HttpResponseHeader());

            webService.doService(httpRequest, httpResponse);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
