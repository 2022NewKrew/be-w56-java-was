package webserver.controller;

import db.DB;
import http.header.HttpHeaders;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.User;

import java.util.List;
import java.util.Map;

public class SignupController extends AbstractController {

    @Override
    public HttpResponse post(HttpRequest request) {
        Map<String, String> queryParams = request.getQueryParams();
        List<String> userProperties = List.of("userId", "password", "name", "email");

        if (!userProperties.stream().allMatch(queryParams::containsKey)) {
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

        return HttpResponse.builder()
                .protocolVersion("HTTP/1.1")
                .headers(new HttpHeaders())
                .status("201 Created")
                .build();
    }
}
