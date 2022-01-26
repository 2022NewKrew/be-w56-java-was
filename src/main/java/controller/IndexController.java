package controller;

import annotation.Controller;
import annotation.RequestMapping;
import http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Controller
public class IndexController {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    private final HttpRequest httpRequest;

    public IndexController(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @RequestMapping(value = "/index.html", method = "GET")
    public HttpResponse getIndexHtml() {
        try {
            File file = new File("./webapp" + httpRequest.getPath());
            byte[] body = Files.readAllBytes(file.toPath());

            HttpHeader responseHeader = new HttpHeader();
            responseHeader.setContentType(MediaType.TEXT_HTML);
            responseHeader.setContentLength(body.length);

            return new HttpResponse("HTTP/1.1", HttpStatus.OK, responseHeader, body);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new HttpResponse("HTTP/1.1", HttpStatus.NOT_FOUND, new HttpHeader());
        }
    }

    @RequestMapping(method = "GET")
    public HttpResponse getIndex() {
        try {
            File file = new File("./webapp/index.html");
            byte[] body = Files.readAllBytes(file.toPath());

            HttpHeader responseHeader = new HttpHeader();
            responseHeader.setContentType(MediaType.TEXT_HTML);
            responseHeader.setContentLength(body.length);

            return new HttpResponse("HTTP/1.1", HttpStatus.OK, responseHeader, body);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new HttpResponse("HTTP/1.1", HttpStatus.NOT_FOUND, new HttpHeader());
        }
    }

}
