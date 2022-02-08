package app.utils;

import com.google.common.net.HttpHeaders;
import webserver.WebServerConfig;
import webserver.http.HttpRequest;

public class SessionUtils {

    public static boolean hasSession(HttpRequest request) {
        String cookieContent = request.getTrailingHeaders().get(HttpHeaders.COOKIE);
        return cookieContent.equals(WebServerConfig.LOGIN_SUCCESS_COOKIE);
    }

}
