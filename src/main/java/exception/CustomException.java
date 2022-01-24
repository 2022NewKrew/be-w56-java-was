package exception;

import lombok.Getter;
import webserver.response.StatusCode;

@Getter
public class CustomException  extends RuntimeException{

    private final StatusCode statusCode;

    public CustomException(StatusCode statusCode) {
        super(statusCode.getDescription());
        this.statusCode = statusCode;
    }

    public CustomException(StatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
