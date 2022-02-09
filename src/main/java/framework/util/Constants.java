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

    /** 현재 프로젝트의 Resource 파일 경로 */
    public static final String CLASS_PATH = PROJECT_PATH.substring(0, PROJECT_PATH.length() - 1) + "src/main/resources/";

    /** 기본 에러 페이지 */
    public static final String DEFAULT_ERROR_PAGE = "/error/error.html";

    /** Controller 경로 */
    public static final String CONTROLLER_PACKAGE = "com.kakao.example.controller";

    /** Redirect 표시 */
    public static final String REDIRECT_MARK = "redirect:";

    /** 기본 HTTP 버전 정보 */
    public static final String DEFAULT_HTTP_VERSION = "HTTP/1.1";

    /** 기본 Connection 정보 */
    public static final String DEFAULT_CONNECTION = "close";

    /** Cookie에 저장될 Session ID의 key 값 */
    public static final String SESSION_ID_KEY = "SESSIONID";

    /** Mustache가 있는지 확인할 때 사용할 정규 표현식 */
    public static final String HAS_MUSTACHE_REGEX = ".*\\{\\{(.+?)\\}\\}.*";

    /** 현재 줄의 Mustache를 각각 가져올 때 사용할 정규 표현식 */
    public static final String EACH_MUSTACHE_REGEX = "\\{\\{(.+?)\\}\\}";

    /** 반복문 또는 if문의 Mustache를 인식하기 위한 정규 표현식 */
    public static final String LOOP_IF_MUSTACHE_REGEX = "\\s*\\{\\{#(.+?)\\}\\}\\s*";

    /** else문의 Mustache를 인식하기 위한 정규 표현식 */
    public static final String ELSE_MUSTACHE_REGEX = "\\s*\\{\\{\\^(.+?)\\}\\}\\s*";

    /** Scope의 끝을 나타내는 Mustache를 인식하기 위한 정규 표현식 */
    public static final String END_MUSTACHE_REGEX = "\\s*\\{\\{/(.+?)\\}\\}\\s*";

    /** Template를 나타내는 Mustache를 인식하기 위한 정규 표현식 */
    public static final String TEMPLATE_MUSTACHE_REGEX = "\\s*\\{\\{>(.+?)\\}\\}\\s*";

    /** Mustache 안의 값을 가져올 때 사용할 index */
    public static final int MUSTACHE_GROUP_INDEX = 1;
}
