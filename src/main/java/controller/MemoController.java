package controller;

import httpmodel.HttpRequest;
import httpmodel.HttpResponse;
import httpmodel.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64.Decoder;
import java.util.Objects;
import model.Memo;
import model.User;
import service.MemoService;
import util.FileConverter;

public class MemoController extends AbstractController {

    private static final String USER = "user";

    private final MemoService memoService;

    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

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
        HttpSession httpSession = httpRequest.getHttpSession();
        User user = (User) httpSession.getAttribute("user");
        String inputMemo = URLDecoder.decode(httpRequest.getRequestBody("memo"), StandardCharsets.UTF_8);
        Memo memo = new Memo(user.getUserId(), inputMemo);
        memoService.save(memo);
        httpResponse.set302Found("/");
    }
}
