package webserver.common;

public enum FileLocation {
    FILE_DIRECTORY("./webapp"),
    INDEX("/index.html"),
    USER_SINGUP_FAIL("/user/signup_failed.html"),
    USER_LOGIN_FAIL("/user/login_failed.html"),
    NONE("/");

    public final String path;

    FileLocation(String path) {
        this.path = path;
    }
}
