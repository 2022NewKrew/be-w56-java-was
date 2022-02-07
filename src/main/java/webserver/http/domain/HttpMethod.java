package webserver.http.domain;

public enum HttpMethod {
    GET("GET"),
    POST("POST");

    private final String value;

    HttpMethod(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
    public boolean isPost() {
        return this == POST;
    }
}
