package framework.controller;

import framework.annotation.Controller;
import framework.annotation.RequestMapping;
import framework.http.enums.MediaType;
import framework.http.request.HttpRequest;
import framework.http.response.HttpResponse;
import framework.http.response.HttpResponseHeader;
import framework.http.response.HttpStatus;

import java.io.IOException;

@Controller
public class FaviconController {
    @RequestMapping(value = "/favicon", method = "GET")
    public HttpResponse getFavicon(HttpRequest httpRequest) throws IOException {
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.IMAGE_AVIF);

        return new HttpResponse(HttpStatus.OK, responseHeader, httpRequest.getPath());
    }
}
