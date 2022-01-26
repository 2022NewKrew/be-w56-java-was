package webserver.requesthandler.httpresponse;

import java.util.stream.Stream;

import static webserver.common.exception.ExceptionMessage.HTTP_STATUS_NOT_FOUND_EXCEPTION;

public enum HttpStatus {
    OK(200),
    FOUND(302);

    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }

    public static Stream<HttpStatus> stream() {
        return Stream.of(HttpStatus.values());
    }

    public static HttpStatus valueOf(int code) throws RuntimeException {
        return HttpStatus.stream()
                .filter(httpStatus -> httpStatus.equals(code))
                .findFirst()
                .orElseThrow(() -> { throw new RuntimeException(HTTP_STATUS_NOT_FOUND_EXCEPTION.getMessage()); });
    }

    public boolean equals(int code) {
        return this.code == code;
    }

    @Override
    public String toString() {
        return this.code + " " + this.name();
    }
}
