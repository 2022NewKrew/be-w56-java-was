package exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import util.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    UNSUPPORTED_METHOD(HttpStatus.BAD_REQUEST, "Unsupported HTTP method"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Oops! An unexpected error seems to have occurred.");

    private final HttpStatus httpStatus;
    private final String message;
}
