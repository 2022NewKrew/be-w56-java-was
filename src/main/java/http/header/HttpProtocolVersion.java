package http.header;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum HttpProtocolVersion {
    HTTP_1_0("HTTP/1.0"),
    HTTP_1_1("HTTP/1.1"),
    ;

    private final String value;

    HttpProtocolVersion(String value) {
        this.value = value;
    }

    public static HttpProtocolVersion parseProtocolVersion(String input) {
        return Arrays.stream(values())
                .filter(x -> x.getValue().equals(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(input));
    }
}
