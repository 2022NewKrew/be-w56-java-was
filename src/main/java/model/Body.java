package model;

import java.util.List;

public class Body {

    public static Body of(List<String> body) {
        return new Body(body);
    }

    private final List<String> body;

    private Body(List<String> body) {

        this.body = body;
    }

    @Override
    public String toString() {
        return "Body{" +
                "body=" + body +
                '}';
    }
}
