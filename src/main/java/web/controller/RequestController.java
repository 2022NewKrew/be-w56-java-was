package web.controller;

import service.PostService;
import service.ViewService;
import web.http.request.HttpRequest;
import web.http.request.HttpRequestLine;
import web.http.response.HttpResponse;
import service.UserService;

import java.io.IOException;

public class RequestController {

    public static HttpResponse getResponse(HttpRequest httpRequest) throws IOException {
        HttpRequestLine requestLine = httpRequest.getHttpRequestLine();
        String url = requestLine.getUrl();
        String method = requestLine.getMethod();

        HttpRequestLineInfo requestLineInfo = HttpRequestLineInfo.lookup(url, method);
        return handleRequest(requestLineInfo, httpRequest);
    }

    public static HttpResponse handleRequest(HttpRequestLineInfo requestLineInfo, HttpRequest httpRequest) throws IOException {
        switch (requestLineInfo){
            case SIGN_UP:
                return UserService.signUp(httpRequest);
            case LOGIN_PAGE:
                return ViewService.loginPage(httpRequest);
            case LOGIN_REQUEST:
                return UserService.login(httpRequest);
            case USER_LIST_PAGE:
                byte[] userListBody = UserService.getUserListBody();
                return ViewService.userListPage(httpRequest, userListBody);
            case POST_REQUEST:
                return PostService.addPostRequest(httpRequest);
            default:
                return ViewService.others(httpRequest);
        }
    }
}
