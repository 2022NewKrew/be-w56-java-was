package exception;

import webserver.response.StatusCode;

public class InvalidRequestException extends CustomException{
    // request를 이해할 수 없음
    public InvalidRequestException(String message) {
        super(StatusCode.BAD_REQUEST, message);
    }
}
