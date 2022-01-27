package framework.util;

import java.io.File;

/**
 * 상수들을 저장
 */
public class Constants {
    /** 현재 프로젝트 경로 */
    public static final String PROJECT_PATH = new File(".").getAbsoluteFile().getPath();

    /** 현재 프로젝트의 static file들이 있는 경로 */
    public static final String CONTEXT_PATH = PROJECT_PATH.substring(0, PROJECT_PATH.length() - 1) + "webapp";

    /** 컨트롤러 경로 */
    public static final String CONTROLLER_PACKAGE = "com.kakao.example.controller";

    /** Redirect 표시 */
    public static final String REDIRECT_MARK = "redirect:";

    /** Redirect할 때의 기본 파일 확장자 */
    public static final String DEFAULT_REDIRECT_EXTENSION = ".html";

    /** 기본 HTTP 버전 정보 */
    public static final String DEFAULT_HTTP_VERSION = "HTTP/1.1";

    /** 기본 Connection 정보 */
    public static final String DEFAULT_CONNECTION = "close";
}
