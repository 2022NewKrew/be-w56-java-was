package webserver.controller;

import db.DB;
import http.exception.NotFound;
import http.request.HttpRequest;
import http.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import model.User;
import webserver.RequestHandler;

import java.util.List;
import java.util.Map;

@Slf4j
public class LoginController extends BaseController {

    @Override
    public HttpResponse post(HttpRequest request) {
        Map<String, String> urlEncodedParams = request.parseUrlEncodedBody();
        List<String> loginProperty = List.of("userId", "password");
        if (!loginProperty.stream().allMatch(urlEncodedParams::containsKey)) {
            log.debug("로그인 실패, 파라미터 부족: {}", urlEncodedParams);
            return HttpResponse.badRequest();
        }

        String userId = urlEncodedParams.get("userId");
        String password = urlEncodedParams.get("password");

        try {
            User user = DB.findUserByIdAndPassword(userId, password);
            Long sessionId = RequestHandler.addSessionUser(user);

            log.debug("로그인 성공, {} sessionId={}", user, sessionId);

            HttpResponse response = HttpResponse.found("/index.html");
            response.getHeaders().add("set-cookie", "logined=true; Path=/");
            response.getHeaders().add("set-cookie", String.format("sessionId=%d; Max-Age=600; Path=/", sessionId));
            return response;
        } catch (NotFound e) {
            log.debug("로그인 실패");

            HttpResponse response = HttpResponse.notFound();
            response.getHeaders().add("set-cookie", "logined=false; Path=/");
            return response;
        }
    }
}
