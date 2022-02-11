package controller;

import annotation.Controller;
import annotation.RequestMapping;
import dto.MemoCreateCommand;
import service.MemoService;
import webserver.ResponseGenerator;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.PathInfo;

@Controller(value = "/memo")
public class MemoController{
    private final MemoService memoService = new MemoService();

    @RequestMapping(value = PathInfo.PATH_MEMO_FORM, requestMethod = "GET")
    public HttpResponse getMemoForm(boolean loggedin) {
        if (loggedin) {
            return ResponseGenerator.generateStaticResponse(PathInfo.PATH_MEMO_FORM_FILE);
        }
        return ResponseGenerator.generateResponse302(PathInfo.PATH_LOGIN_PAGE);
    }

    @RequestMapping(value = PathInfo.PATH_MEMO_CREATE_REQUEST, requestMethod = "POST")
    public HttpResponse createMemo(String writer, String contents) {
        try {
            MemoCreateCommand mcc = MemoCreateCommand.builder()
                    .writer(writer)
                    .contents(contents)
                    .build();
            memoService.store(mcc);
            return ResponseGenerator.generateResponse302(PathInfo.PATH_INDEX);
        } catch (Exception e) {
            return ResponseGenerator.generateResponse400(e);
        }
    }
}