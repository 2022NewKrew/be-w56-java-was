package webserver.controller;

import db.DB;
import http.header.HttpHeaders;
import http.header.HttpProtocolVersions;
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
        Map<String, String> queryParams = request.parseUrlEncodedBody();
        List<String> userProperties = List.of("userId", "password", "name", "email");

        if (!userProperties.stream().allMatch(queryParams::containsKey)) {
            log.debug("회원가입 실패: {}", queryParams);
            return HttpResponse
                    .builder()
                    .protocolVersion(HttpProtocolVersions.HTTP_1_1.getValue())
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
        log.debug("회원가입 성공: {}", user);

        return HttpResponse.found("/index.html");
    }
}
