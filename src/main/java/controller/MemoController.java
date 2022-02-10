package controller;

import http.Request;
import http.Response;
import model.Memo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.MemoService;
import service.UserService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;

public class MemoController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(MemoController.class);

    @Override
    public void makeResponse(Request request, Response response) {
        boolean checkLogin = UserService.checkLogin(request);
        log.debug("checkLogin: {}", checkLogin);

        if (checkLogin) {
            Map<String, String> newMemo = request.getBody();
            String writer = newMemo.get("writer");
            String title = URLDecoder.decode(newMemo.get("title"), StandardCharsets.UTF_8);
            String content = URLDecoder.decode(newMemo.get("content"), StandardCharsets.UTF_8);

            Memo memo = new Memo(LocalDate.now(), writer, title, content);
            MemoService.create(memo);
            response.redirectResponse("/");
        } else {
            response.redirectResponse("/user/login.html");
        }
    }
}
