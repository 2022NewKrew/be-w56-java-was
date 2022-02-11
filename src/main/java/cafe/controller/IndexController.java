package cafe.controller;

import cafe.dto.QnaDto;
import cafe.service.QnaService;
import cafe.view.QnaView;
import framework.annotation.Controller;
import framework.annotation.RequestMapping;
import framework.http.enums.MediaType;
import framework.http.request.HttpRequest;
import framework.http.response.HttpResponse;
import framework.http.response.HttpResponseHeader;
import framework.http.response.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class IndexController {
    private final QnaService qnaService;
    private final QnaView qnaView;

    public IndexController() {
        this.qnaService = new QnaService();
        this.qnaView = new QnaView();
    }

    @RequestMapping(value = "/index.html", method = "GET")
    public HttpResponse getIndexHtml(HttpRequest httpRequest) throws IOException {
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        List<QnaDto> qnaList = qnaService.getQnaList();
        String qnaListHtml = qnaView.getQnaListHtml(qnaList);

        return new HttpResponse(HttpStatus.OK, responseHeader, qnaListHtml.getBytes(StandardCharsets.UTF_8));
    }

    @RequestMapping(method = "GET")
    public HttpResponse getIndex() throws IOException {
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        List<QnaDto> qnaList = qnaService.getQnaList();
        String qnaListHtml = qnaView.getQnaListHtml(qnaList);

        return new HttpResponse(HttpStatus.OK, responseHeader, qnaListHtml.getBytes(StandardCharsets.UTF_8));
    }

}
