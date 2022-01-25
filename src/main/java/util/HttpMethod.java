package util;

public enum HttpMethod {
    NONE(""), GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

}
