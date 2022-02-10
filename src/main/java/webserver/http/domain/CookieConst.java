package webserver.http.domain;

public enum CookieConst {
    LOGIN_COOKIE("logined");

    private final String value;

    CookieConst(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
