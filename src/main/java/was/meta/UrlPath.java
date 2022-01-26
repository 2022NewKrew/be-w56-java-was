package was.meta;

import was.domain.http.HttpRequest;

import java.util.Arrays;

public enum UrlPath {
    HOME("GET", "/"),
    LOGIN_FORM("GET", "/login.html"),
    LOGIN_FAIL("GET", "/login_fail.html"),
    LOGIN("POST", "/users/login"),
    SIGN_UP("POST", "/users");

    private final String method;
    private final String path;

    UrlPath(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public static UrlPath findByMethodAndPath(HttpRequest req) {
        return Arrays.stream(UrlPath.values())
                .filter(urlPath -> isSameMethodAndPath(req, urlPath))
                .findAny()
                .orElse(null);
    }

    private static boolean isSameMethodAndPath(HttpRequest req, UrlPath urlPath) {
        return urlPath.path.equals(req.getPath()) && urlPath.method.equals(req.getMethod());
    }
}
