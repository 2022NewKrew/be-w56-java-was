package http;

import java.util.Objects;

public class StatusLine {
    private final HttpVersion version;
    private final HttpStatus status;

    private StatusLine(HttpVersion version, HttpStatus status) {
        validateNull(version, status);
        this.version = version;
        this.status = status;
    }

    public static StatusLine create(HttpStatus status) {
        return new StatusLine(HttpVersion.V_1_1, status);
    }

    private void validateNull(HttpVersion version, HttpStatus status) {
        if (version == null || status == null) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        StatusLine line = (StatusLine) object;
        return version == line.version && status == line.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, status);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public HttpVersion getVersion() {
        return version;
    }
}
