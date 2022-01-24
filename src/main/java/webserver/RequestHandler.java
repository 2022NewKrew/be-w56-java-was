package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import http.HttpRequest;
import http.HttpResponse;
import http.Resource;
import http.StaticFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    public static final String DOCUMENT_ROOT = "./webapp";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = new HttpRequest(in);
            httpRequest.loggingRequestHeader();
            String requestUri = httpRequest.getUri();

            HttpResponse httpResponse = new HttpResponse(new DataOutputStream(out));
            String contentType = getContentType(httpRequest);
            Resource file = getStaticFile(contentType, requestUri);
            httpResponse.send(file);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String getContentType(HttpRequest httpRequest) {
        String acceptHeader = httpRequest.getHeader("Accept").orElse("text/html");
        String contentType = acceptHeader.split(",")[0].trim();
        return contentType;
    }

    private Resource getStaticFile(String contentType, String requestUri) {
        if ("/".equals(requestUri)) {
            return new StaticFile(contentType, new File(DOCUMENT_ROOT + "/index.html"));
        }

        return new StaticFile(contentType, new File(DOCUMENT_ROOT + requestUri));
    }
}
