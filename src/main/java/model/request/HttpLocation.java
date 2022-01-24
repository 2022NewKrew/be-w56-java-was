package model.request;

import java.util.Objects;

public class HttpLocation {
    private final String location;

    public HttpLocation(final String location) {
        Objects.requireNonNull(location);
        validate(location);
        this.location = location;
    }

    private void validate(final String location) {
        if (location.isBlank() || !location.startsWith("/")) {
            throw new IllegalStateException("HttpLocation is invalid! - " + location);
        }
    }

    public String getLocation() {
        return this.location;
    }
}
