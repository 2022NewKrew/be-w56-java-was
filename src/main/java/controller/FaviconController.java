package controller;

import annotation.Controller;
import annotation.RequestMapping;
import http.HttpHeader;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Controller
public class FaviconController {
    private static final Logger log = LoggerFactory.getLogger(FaviconController.class);

    private final HttpRequest httpRequest;

    public FaviconController(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @RequestMapping(value = "/favicon", method = "GET")
    public HttpResponse getFavicon() {
        try {
            File file = new File("./webapp" + httpRequest.getRequestLine().getPath());
            byte[] body = Files.readAllBytes(file.toPath());

            HttpHeader responseHeader = new HttpHeader();
            responseHeader.addHeader("Content-Type", "image/avif");
            responseHeader.addHeader("Content-Length", String.valueOf(body.length));

            return new HttpResponse("HTTP/1.1", HttpStatus.OK, responseHeader, body);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new HttpResponse("HTTP/1.1", HttpStatus.NOT_FOUND, new HttpHeader());
        }
    }
}
