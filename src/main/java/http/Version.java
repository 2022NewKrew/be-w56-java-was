package http;

import java.util.Optional;

public enum Version {
    HTTP_1_0("HTTP/1.0"),
    HTTP_1_1("HTTP/1.1");

    private final String version;

    Version(String version) {
        this.version = version;
    }

    public static Optional<Version> fromString(String version) {
        for (Version v : values()) {
            if (v.version.equals(version)) {
                return Optional.of(v);
            }
        }
        return Optional.empty();
    }
}
