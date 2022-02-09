package model;

public class Body {

    public static Body of(String body) {
        return new Body(body.trim());
    }

    private final String body;

    private Body(String body) {
        this.body = body;
    }

    public String get() {
        return body;
    }

    @Override
    public String toString() {
        return "Body{" +
                "body=" + body +
                '}';
    }
}
