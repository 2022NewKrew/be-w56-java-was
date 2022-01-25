package controller.view;

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
public class ViewController {
    private static final Logger log = LoggerFactory.getLogger(ViewController.class);

    private final HttpRequest httpRequest;

    public ViewController(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @RequestMapping(value = "/view", method = "GET")
    public HttpResponse getView() {
        try {
            File file = new File("./webapp" + httpRequest.getRequestLine().getPath());
            byte[] body = Files.readAllBytes(file.toPath());

            HttpHeader header = new HttpHeader();
            header.addHeader("Content-Type", "text/html");
            header.addHeader("Content-Length", String.valueOf(body.length));

            return new HttpResponse("HTTP/1.1", HttpStatus.OK, header, body);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new HttpResponse("HTTP/1.1", HttpStatus.NOT_FOUND, new HttpHeader());
        }
    }
}
