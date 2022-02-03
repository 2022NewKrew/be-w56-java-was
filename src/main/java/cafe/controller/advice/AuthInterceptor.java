package cafe.controller.advice;

import framework.annotation.Interceptor;
import framework.annotation.PreHandle;
import framework.http.request.HttpRequest;
import framework.http.response.HttpResponse;
import framework.http.response.HttpResponseHeader;
import framework.http.response.HttpStatus;

import java.util.Arrays;

@Interceptor
public class AuthInterceptor {
    private static final String COOKIE_PARSE_DELIMITER = "; ";
    private static final String LOGIN_COOKIE_STRING = "logined=true";

    @PreHandle(includePathPattern = {"/user/list.html", "/user/profile.html"})
    public boolean loginPreHandle(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (!isLoginedUser(httpRequest.getCookie())) {
            HttpResponseHeader responseHeader = httpResponse.getHeader();
            httpResponse.setHttpStatus(HttpStatus.FOUND);
            responseHeader.setLocation("/user/login.html");
            return false;
        }

        return true;
    }

    private boolean isLoginedUser(String cookie) {
        return Arrays.stream(cookie.split(COOKIE_PARSE_DELIMITER))
                .anyMatch(cookieString -> cookieString.equals(LOGIN_COOKIE_STRING));
    }
}
