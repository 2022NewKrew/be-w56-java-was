package http.header;

import lombok.Getter;

@Getter
public enum HttpProtocolVersions {
    HTTP_1_0("HTTP/1.0"),
    HTTP_1_1("HTTP/1.1"),
    ;

    private final String value;

    HttpProtocolVersions(String value) {
        this.value = value;
    }
}
