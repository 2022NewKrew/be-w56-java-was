package webserver;

import java.io.*;
import java.net.Socket;

import http.HttpRequest;
import http.HttpResponse;
import http.exception.BadHttpFormatException;
import http.HttpRequestParser;
import http.HttpResponseRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.processor.HttpProcessor;

public class RequestHandler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("Connection Open IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        HttpProcessor processor = HttpFactory.httpProcessor();
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = parseHttpRequest(in);
            HttpResponse response = processor.process(httpRequest);
            ByteArrayOutputStream bos = renderOutputStream(response);
            bos.writeTo(out);
        } catch (BadHttpFormatException e) {
            log.info("Not Http Request : {}", e.getMessage());
        } catch (IOException e) {
            log.info("Already Socket Closed or Socket Connection Refused : {}", e.getMessage());
        } catch (Exception e) {
            log.info("Unknown Exception");
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("Connection Closed IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        }
    }

    private HttpRequest parseHttpRequest(InputStream in) throws IOException {
        HttpRequestParser parser = HttpFactory.httpRequestParser();
        return parser.parse(in);
    }

    private ByteArrayOutputStream renderOutputStream(HttpResponse httpResponse) {
        HttpResponseRenderer httpResponseRenderer = HttpFactory.httpResponseRenderer();
        return httpResponseRenderer.render(httpResponse);
    }
}
