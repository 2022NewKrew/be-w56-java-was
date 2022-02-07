package cafe.controller;

import cafe.service.QnaService;
import framework.annotation.Controller;
import framework.annotation.RequestMapping;
import framework.http.enums.MediaType;
import framework.http.request.HttpRequest;
import framework.http.response.HttpResponse;
import framework.http.response.HttpResponseHeader;
import framework.http.response.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class IndexController {
    private final QnaService qnaService;

    public IndexController() {
        this.qnaService = new QnaService();
    }

    @RequestMapping(value = "/index.html", method = "GET")
    public HttpResponse getIndexHtml(HttpRequest httpRequest) throws IOException {
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        String qnaListHtml = qnaService.getQnaListHtml();

        return new HttpResponse(HttpStatus.OK, responseHeader, qnaListHtml.getBytes(StandardCharsets.UTF_8));
    }

    @RequestMapping(method = "GET")
    public HttpResponse getIndex() throws IOException {
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        String qnaListHtml = qnaService.getQnaListHtml();

        return new HttpResponse(HttpStatus.OK, responseHeader, qnaListHtml.getBytes(StandardCharsets.UTF_8));
    }

}
