package cafe.controller;

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
public class IndexController {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    private final HttpRequest httpRequest;

    public IndexController(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @RequestMapping(value = "/index.html", method = "GET")
    public HttpResponse getIndexHtml() throws IOException {
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        return new HttpResponse(HttpStatus.OK, responseHeader, httpRequest.getPath());
    }

    @RequestMapping(method = "GET")
    public HttpResponse getIndex() throws IOException {
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        return new HttpResponse(HttpStatus.OK, responseHeader, "/index.html");
    }

}
