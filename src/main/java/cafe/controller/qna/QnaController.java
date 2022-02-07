package cafe.controller.qna;

import cafe.dto.QnaCreateDto;
import cafe.service.QnaService;
import framework.annotation.Controller;
import framework.annotation.RequestBody;
import framework.annotation.RequestMapping;
import framework.http.enums.MediaType;
import framework.http.request.HttpRequest;
import framework.http.response.HttpResponse;
import framework.http.response.HttpResponseHeader;
import framework.http.response.HttpStatus;

import java.io.IOException;

@Controller
public class QnaController {
    private final QnaService qnaService;

    public QnaController() {
        this.qnaService = new QnaService();
    }

    @RequestMapping(value = "/qna/form.html", method = "GET")
    public HttpResponse getCreateQnaForm(HttpRequest httpRequest) throws IOException {
        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);

        return new HttpResponse(HttpStatus.OK, responseHeader, httpRequest.getPath());
    }

    @RequestMapping(value = "/qna", method = "POST")
    public HttpResponse createQna(@RequestBody QnaCreateDto qnaCreateDto) throws IOException {
        qnaService.makeQna(qnaCreateDto);

        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);
        responseHeader.setLocation("/index.html");

        return new HttpResponse(HttpStatus.FOUND, responseHeader);
    }

}
