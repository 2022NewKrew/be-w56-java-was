package controller;

public enum HttpRequestLineInfo {

    SIGN_UP("/user/create", "GET", true),
    OTHERS(null, "GET", false);

    private final String url;
    private final String method;
    private final boolean hasQueries;

    HttpRequestLineInfo(String url, String method, boolean hasQueries) {
        this.url = url;
        this.method = method;
        this.hasQueries = hasQueries;
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
