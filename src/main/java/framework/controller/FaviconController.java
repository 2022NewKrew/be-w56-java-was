package framework.controller;

import framework.annotation.Controller;
import framework.annotation.RequestMapping;
import framework.http.enums.HttpStatus;
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
public class FaviconController {
    private static final Logger log = LoggerFactory.getLogger(FaviconController.class);

    private final HttpRequest httpRequest;

    public FaviconController(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @RequestMapping(value = "/favicon", method = "GET")
    public HttpResponse getFavicon() throws IOException {
        File file = new File("./webapp" + httpRequest.getPath());
        byte[] body = Files.readAllBytes(file.toPath());

        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.IMAGE_AVIF);

        return new HttpResponse(HttpStatus.OK, responseHeader, body);
    }
}
