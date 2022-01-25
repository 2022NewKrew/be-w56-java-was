package model.request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class HttpLocation {
    private final String location;

    public HttpLocation(final String percentEncodedLocation) {
        Objects.requireNonNull(percentEncodedLocation);
        validate(percentEncodedLocation);
        this.location = URLDecoder.decode(percentEncodedLocation, StandardCharsets.UTF_8);
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
