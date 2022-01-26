package model.request;

import java.util.Objects;

public class Body {
    public static final Body EMPTY = new Body("");

    private final String body;

    public Body(final String body) {
        this.body = Objects.requireNonNull(body).trim();
    }

    public String get() {
        return this.body;
    }
}
