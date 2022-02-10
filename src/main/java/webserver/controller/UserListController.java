package webserver.controller;

import db.DB;
import db.UserCache;
import http.cookie.Cookie;
import http.exception.NotFound;
import http.request.HttpRequest;
import http.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import model.User;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class UserListController extends BaseController {

    private static final String REPLACER = "\\{\\{#userList}}";

    @Override
    public HttpResponse get(HttpRequest request) {
        // TODO: Interceptor로 구현
        try {
            List<Cookie> cookies = request.getCookies();
            Long sessionId = cookies.stream()
                    .filter(x -> x.getName().equals("sessionId"))
                    .findAny()
                    .map(x -> Long.parseLong(x.getValue()))
                    .orElseThrow(NotFound::new);
            User sessionUser = UserCache.getSessionUser(sessionId);

            log.debug("세션 유저: {}", sessionUser);

            StringBuilder userListBody = new StringBuilder();

            int index = 0;
            for (User user: DB.findAll()) {
                userListBody.append("<tr>")
                        .append("<th scope=\"row\">").append(++index).append("</th>")
                        .append("<td>").append(user.getUserId()).append("</td>")
                        .append("<td>").append(user.getName()).append("</td>")
                        .append("<td>").append(user.getEmail()).append("</td>")
                        .append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>")
                        .append("</tr>");
            }

            return HttpResponse.okTemplate("/user/list.vhtml", Pattern.compile(REPLACER), userListBody.toString());
        } catch (Exception e) {
            log.debug(e.getMessage());
            return HttpResponse.redirect("/user/login.html");
        }
    }
}
