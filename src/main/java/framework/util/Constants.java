package framework.util;

import java.io.File;

public class Constants {
    public static final String PROJECT_PATH = new File(".").getAbsoluteFile().getPath();
    public static final String CONTEXT_PATH = PROJECT_PATH.substring(0, PROJECT_PATH.length() - 1) + "webapp";
    public static final String CONTROLLER_PACKAGE = "com.kakao.example.controller";
    public static final String REDIRECT_MARK = "redirect:";
    public static final String DEFAULT_REDIRECT_EXTENSION = ".html";

    public static final String DEFAULT_HTTP_VERSION = "HTTP/1.1";
    public static final String DEFAULT_CONNECTION = "close";
}
