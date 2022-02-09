package controller;

import controller.util.HtmlResponseUtils;
import domain.memo.dto.MemoCreate;
import domain.memo.dto.MemoInfo;
import domain.memo.service.MemoService;
import http.HttpHeader;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class MemoController implements Controller {

    private static final String MEMO_LIST_PATH = "/";
    private static final String MEMO_CREATE_PATH = "/";

    private static final String USER_LOGIN_URL = "/user/login.html";

    private final MemoService memoService;

    public static MemoController create() {
        return new MemoController(MemoService.create());
    }

    private MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    @Override
    public HttpResponse doGet(HttpRequest request) {
        String path = request.getPath();
        if (!path.equals(MEMO_LIST_PATH)) {
            return Controller.super.doGet(request);
        }

        List<MemoInfo> memoInfos = memoService.readAll();

        String body = HtmlResponseUtils.generateMemosHtml(memoInfos);

        return HttpResponse.builder()
            .status(HttpStatus.OK)
            .responseBody(body.getBytes(StandardCharsets.UTF_8))
            .build();
    }

    @Override
    public HttpResponse doPost(HttpRequest request) {
        String path = request.getPath();
        if (!path.equals(MEMO_CREATE_PATH)) {
            return Controller.super.doPost(request);
        }

        if (!request.isLogined()) {
            HttpHeader httpHeader = HttpHeader.of(Map.of("Location", USER_LOGIN_URL));

            return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .header(httpHeader)
                .build();
        }

        memoService.create(MemoCreate.builder()
            .author(request.getBodyParameter("author"))
            .content(request.getBodyParameter("content"))
            .build());

        HttpHeader httpHeader = HttpHeader.of(Map.of("Location", MEMO_LIST_PATH));

        return HttpResponse.builder()
            .status(HttpStatus.FOUND)
            .header(httpHeader)
            .build();
    }
}
