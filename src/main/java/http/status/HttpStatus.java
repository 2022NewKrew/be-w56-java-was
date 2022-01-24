package http.status;

import lombok.Getter;

@Getter
public enum HttpStatus {
    OK(200, "OK");

    private final int statusCode;
    private final String statusMessage;
    HttpStatus(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
