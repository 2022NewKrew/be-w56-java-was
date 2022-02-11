package config;

public class config {
    // page path
    public static final String DEFAULT_PATH = "./webapp";
    public static final String MAIN_PAGE = "./webapp/index.html";
    public static final String LOGIN_FAIL_PAGE = "./webapp/user/login_failed.html";

    // uri
    public static final String DEFAULT_URI = "/";
    public static final String LOGIN_FAIL_URI = "/user/login_failed.html";

    // cookie
    public static final String LOGIN_SUCCESS_COOKIE = "logined=true; Path=/";
    public static final String LOGIN_FAIL_COOKIE = "logined=false; Path=/";
}
