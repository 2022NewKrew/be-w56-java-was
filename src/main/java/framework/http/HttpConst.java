package framework.http;

public class HttpConst {
    public static final String STATIC_ROOT = "./webapp";
    public static final String ERROR_PAGE = "/error.html";
    public static final String LOGIN_PAGE = "/user/login.html";
    public static final String LOGIN_FAIL_PAGE = "/user/login_failed.html";

    // HTTP Header Line
    public static final int REQUEST_METHOD = 0;
    public static final int REQUEST_URI = 1;

    // HTTP URI
    public static final int URI_PATH = 0;
    public static final int URI_QUERY_PARAM = 1;

    // HTTP Cookie
    public static final String COOKIE_LOGINED = "logined";
}
