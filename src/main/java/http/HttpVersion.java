package http;

import java.util.Arrays;
import java.util.Optional;

public enum HttpVersion {
    V_1_0("HTTP/1.0"),
    V_1_1("HTTP/1.1"),
    NONE("HTTP/1.1");

    private final String value;

    HttpVersion(String version) {
        this.value = version;
    }

    public static HttpVersion matchValue(String value) {
        Optional<HttpVersion> findMethod = Arrays.stream(values())
                .filter(version -> version.value.equals(value))
                .findAny();

        if(findMethod.isEmpty()) {
            return HttpVersion.NONE;
        }
        return findMethod.get();
    }

    public String getValue() {
        return value;
    }
}
