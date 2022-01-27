package framework.controller;

import framework.annotation.Controller;
import framework.annotation.RequestMapping;
import framework.http.*;
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
            File file = new File("./webapp" + httpRequest.getPath());
            byte[] body = Files.readAllBytes(file.toPath());

            HttpHeader responseHeader = new HttpHeader();
            responseHeader.setContentType(MediaType.IMAGE_AVIF);

            return new HttpResponse("HTTP/1.1", HttpStatus.OK, responseHeader, body);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new HttpResponse("HTTP/1.1", HttpStatus.NOT_FOUND, new HttpHeader());
        }
    }
}
