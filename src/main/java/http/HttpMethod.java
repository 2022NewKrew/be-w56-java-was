package http;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private String code;

    HttpMethod(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
