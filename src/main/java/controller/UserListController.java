package controller;

import http.request.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import renderer.UserListPageRenderer;

import java.util.Optional;

public class UserListController implements Controller{

    private final UserListPageRenderer userListPageRenderer;

    public UserListController(UserListPageRenderer userListPageRenderer) {
        this.userListPageRenderer = userListPageRenderer;
    }

    @Override
    public boolean isSupported(HttpRequest httpRequest) {
        if ("/user/list".equals(httpRequest.getPath()) && httpRequest.isMethod(HttpMethod.GET)) {
            return true;
        }

        return false;
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (isNotLoginUser(getLoginedCookie(httpRequest))) {
            httpResponse.redirect("/user/login.html");
            return;
        }

        httpResponse.body(userListPageRenderer.makeUserListPage());
    }

    private Optional<String> getLoginedCookie(HttpRequest httpRequest) {
        Optional<String> logined = httpRequest.getCookie().
                flatMap((cookie -> cookie.getAttribute("logined")));
        return logined;
    }

    private boolean isNotLoginUser(Optional<String> logined) {
        return logined.filter("true"::equals).isEmpty();
    }
}
