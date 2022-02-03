package framework.controller;

import framework.annotation.Controller;
import framework.annotation.RequestMapping;
import framework.http.enums.MediaType;
import framework.http.request.HttpRequest;
import framework.http.response.HttpResponse;
import framework.http.response.HttpResponseHeader;
import framework.http.response.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Controller
public class FaviconController {
    private static final Logger log = LoggerFactory.getLogger(FaviconController.class);

    private final HttpRequest httpRequest;

    public FaviconController(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @RequestMapping(value = "/favicon", method = "GET")
    public HttpResponse getFavicon() throws IOException {
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.IMAGE_AVIF);

        return new HttpResponse(HttpStatus.OK, responseHeader, httpRequest.getPath());
    }
}
