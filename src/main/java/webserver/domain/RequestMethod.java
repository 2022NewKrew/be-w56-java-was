package webserver.domain;

public enum RequestMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    NULL("NULL");

    private String methodName;

    private RequestMethod(String methodName) {
        this.methodName = methodName;
    }
}
