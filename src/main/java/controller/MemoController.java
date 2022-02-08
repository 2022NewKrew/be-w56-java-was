package controller;

import httpmodel.HttpRequest;
import httpmodel.HttpResponse;
import httpmodel.HttpSession;
import java.io.IOException;
import java.util.Objects;
import util.FileConverter;

public class MemoController extends AbstractController {

    private static final String USER = "user";

//    private final MemoService memoService;
//
//    public MemoController(MemoService memoService) {
//        this.memoService = memoService;
//    }

    @Override
    protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        HttpSession httpSession = httpRequest.getHttpSession();
        if (Objects.isNull(httpSession.getAttribute(USER))) {
            httpResponse.set302Found("/user/login.html");
            return;
        }
        byte[] responseBody = FileConverter.fileToString("/qna/form.html");
        httpResponse.set200OK(httpRequest, responseBody);
    }

    @Override
    protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        // 디비 추가 로직
    }
}
