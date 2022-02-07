package controller;

import http.Cookie;
import http.request.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import renderer.UserListPageRenderer;

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
        Cookie cookie = httpRequest.getCookie().orElseThrow(() -> new IllegalArgumentException("로그인 되지 않은 사용자 입니다."));
        String logined = cookie.getAttribute("logined").orElseThrow(() -> new IllegalArgumentException("로그인 되지 않은 사용자 입니다."));
        if (!"true".equals(logined)) {
            throw new IllegalArgumentException("로그인 되지 않은 사용자 입니다.");
        }

        httpResponse.body(userListPageRenderer.makeUserListPage());
    }
}
