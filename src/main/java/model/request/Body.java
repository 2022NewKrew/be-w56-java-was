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

    public boolean isNotEmpty() {
        return !this.equals(EMPTY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Body body1 = (Body) o;
        return body.equals(body1.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(body);
    }
}
