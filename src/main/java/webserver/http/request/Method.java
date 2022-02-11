package webserver.http.request;

public enum Method {
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    TRACE("TRACE"),
    CONNECT("CONNECT"),
    UNRECOGNIZED("EMPTY");

    private final String method;

    Method(String method) {
        this.method = method;
    }
}
