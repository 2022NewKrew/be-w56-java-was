package http;

import javax.annotation.Nullable;

public enum Version {
    HTTP_1_0("HTTP/1.0"),
    HTTP_1_1("HTTP/1.1");

    private final String version;

    Version(String version) {
        this.version = version;
    }

    @Nullable
    public static Version fromString(String version) {
        for (Version v : values()) {
            if (v.version.equals(version)) {
                return v;
            }
        }
        return null;
    }
}
