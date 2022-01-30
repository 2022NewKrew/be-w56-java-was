package cafe.controller;

import framework.annotation.Controller;
import framework.annotation.RequestMapping;
import framework.http.response.HttpStatus;
import framework.http.enums.MediaType;
import framework.http.request.HttpRequest;
import framework.http.response.HttpResponse;
import framework.http.response.HttpResponseHeader;
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
    public HttpResponse getIndexHtml() throws IOException {
        File file = new File("./webapp" + httpRequest.getPath());
        byte[] body = Files.readAllBytes(file.toPath());

        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        return new HttpResponse(HttpStatus.OK, responseHeader, body);
    }

    @RequestMapping(method = "GET")
    public HttpResponse getIndex() throws IOException {
        File file = new File("./webapp/index.html");
        byte[] body = Files.readAllBytes(file.toPath());

        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);
        responseHeader.setContentLength(body.length);

        return new HttpResponse(HttpStatus.OK, responseHeader, body);
    }

}
