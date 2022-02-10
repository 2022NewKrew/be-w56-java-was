package web.controller;

public enum HttpRequestLineInfo {

    SIGN_UP("/user/create", "POST"),
    LOGIN_PAGE("/user/login", "GET"),
    LOGIN_REQUEST("/user/login", "POST"),
    USER_LIST_PAGE("/user/list", "GET"),

    POST_REQUEST("/post/create", "POST"),

    INDEX("/index", "GET"),
    
    OTHERS(null, "GET");

    private final String url;
    private final String method;

    HttpRequestLineInfo(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public static HttpRequestLineInfo lookup(String url, String method){
        for(HttpRequestLineInfo info: HttpRequestLineInfo.values()){
            if (!info.equals(OTHERS) && url.startsWith(info.url) && method.equals(info.method)) {
                return info;
            }
        }
        return OTHERS;
    }
}
