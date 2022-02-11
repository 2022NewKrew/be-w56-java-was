package util.constant;

public enum Route {
    BASE("./webapp"),
    INDEX("/index.html"),
    LOGIN_SUCCESS("/user/login.html"),
    LOGIN_FAILED("/user/login_failed.html");

    private final String path;

    Route(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
