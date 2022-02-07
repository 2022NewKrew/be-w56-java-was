package model;

public enum HttpMethod {
    GET(false),
    POST(true);

    private final boolean body;

    HttpMethod(boolean body) {
        this.body = body;
    }

    public static HttpMethod fromString(String method) {
        for (HttpMethod m : HttpMethod.values()) {
            if (m.name().equalsIgnoreCase(method)) {
                return m;
            }
        }
        throw new Error();
    }
}
