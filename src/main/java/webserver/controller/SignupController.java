package webserver.controller;

import db.DB;
import http.header.HttpHeaders;
import http.request.HttpRequest;
import http.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import model.User;

import java.util.List;
import java.util.Map;

@Slf4j
public class SignupController extends BaseController {

    @Override
    public HttpResponse post(HttpRequest request) {
        // FIXME: post는 body에서 가져와야 함 (Content-Type: application/x-www-form-urlencoded)
        Map<String, String> queryParams = request.parseUrlEncodedBody();
        List<String> userProperties = List.of("userId", "password", "name", "email");

        if (!userProperties.stream().allMatch(queryParams::containsKey)) {
            log.debug("회원가입 실패");
            log.debug(queryParams.toString());
            return HttpResponse
                    .builder()
                    .protocolVersion("HTTP/1.1")
                    .headers(new HttpHeaders())
                    .status("400 Bad Request")
                    .build();
        }

        User user = User.builder()
                .userId(queryParams.get("userId"))
                .password(queryParams.get("password"))
                .name(queryParams.get("name"))
                .email(queryParams.get("email"))
                .build();

        DB.addUser(user);
        log.debug("회원가입 성공");
        log.debug(user.toString());

        return HttpResponse.found("/index.html");
    }
}
