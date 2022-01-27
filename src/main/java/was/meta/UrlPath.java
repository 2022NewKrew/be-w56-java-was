package was.meta;

import was.http.HttpRequest;

import java.util.Arrays;

public enum UrlPath {

    HOME("GET", "/"),
    SIGNUP("POST", "/users/signup"),
    LOGIN("POST", "/users/login"),

    INDEX("GET", "/index.html"),
    SIGNUP_FORM("GET", "/user/form.html"),
    LOGIN_FORM("GET", "/user/login.html"),
    LOGIN_FAILED("GET", "/user/login_failed.html"),
    ERROR_NOT_FOUND("GET", "/error_404.html"),
    ERROR_INTERNAL_SERVER_ERROR("GET", "/error_500.html");

    private String method;
    private String path;

    UrlPath(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public static UrlPath findUrlPathByHttpRequest(HttpRequest request) {
        return Arrays.stream(UrlPath.values())
                .filter(urlPath -> urlPath.matchMethodAndPath(request))
                .findFirst()
                .orElse(null);
    }

    public boolean matchMethodAndPath(HttpRequest request) {
        return method.equals(request.getMethod()) && path.equals(request.getPath());
    }
}
