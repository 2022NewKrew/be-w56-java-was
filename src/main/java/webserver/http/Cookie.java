package webserver.http;

import util.HttpRequestUtils;

import java.util.Map;

public class Cookie {
    private String userId = "";
    private Boolean login = false;

    public Cookie(String cookie) {
        setCookie(cookie);
    }

    private void setCookie(String cookie) {
        Map<String, String> parameters = HttpRequestUtils.parseCookies(cookie);
        String userIdString = parameters.get("userId");
        if (userIdString != null && !userIdString.equals("")) {
            login = true;
            userId = parameters.get("userId");
        }
    }

    public String getUserId() {
        return userId;
    }

    public Boolean getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "userId='" + userId + '\'' +
                ", login=" + login +
                '}';
    }
}
