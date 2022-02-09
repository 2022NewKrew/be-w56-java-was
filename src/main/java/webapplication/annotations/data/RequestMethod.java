package webapplication.annotations.data;

public enum RequestMethod {
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE");

    private String code;

    RequestMethod(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
