package webserver.controller;

import db.DB;
import http.header.HttpHeaders;
import http.header.HttpProtocolVersion;
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
        Map<String, String> urlEncodedParams = request.parseUrlEncodedBody();
        List<String> userProperties = List.of("userId", "password", "name", "email");

        if (!userProperties.stream().allMatch(urlEncodedParams::containsKey)) {
            log.debug("회원가입 실패: {}", urlEncodedParams);
            return HttpResponse
                    .builder()
                    .protocolVersion(HttpProtocolVersion.HTTP_1_1)
                    .headers(new HttpHeaders())
                    .status("400 Bad Request")
                    .build();
        }

        User user = User.builder()
                .userId(urlEncodedParams.get("userId"))
                .password(urlEncodedParams.get("password"))
                .name(urlEncodedParams.get("name"))
                .email(urlEncodedParams.get("email"))
                .build();

        DB.addUser(user);
        log.debug("회원가입 성공: {}", user);

        return HttpResponse.found("/index.html");
    }
}
