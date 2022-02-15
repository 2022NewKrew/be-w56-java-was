package webserver.http.response.header;

import lombok.Getter;

@Getter
public enum Status {
    RESPONSE200(200, "OK "),
    RESPONSE302(302, "FOUND ");

    private final int code;
    private final String message;

    Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
