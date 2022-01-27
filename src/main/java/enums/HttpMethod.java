package enums;

public enum HttpMethod {
    GET("GET"), POST("POST"), DELETE("DELETE");

    private final String name;

    HttpMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
