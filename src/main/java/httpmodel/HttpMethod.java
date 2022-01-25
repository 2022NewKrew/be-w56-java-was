package httpmodel;

public enum HttpMethod {
    GET(false),
    POST(true);

    private final boolean body;

    HttpMethod(boolean body) {
        this.body = body;
    }

    public boolean isBody() {
        return body;
    }
}
