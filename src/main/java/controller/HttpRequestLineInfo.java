package controller;

public enum HttpRequestLineInfo {

    SIGN_UP("/user/create", "POST"),
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
