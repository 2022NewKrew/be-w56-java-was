package exception;

import model.RequestHeader;
import model.ResponseHeader;
import model.builder.ExceptionResponseBuilder;

import java.io.IOException;

public class ExceptionHandler {

    private ExceptionHandler() {
    }

    public static ResponseHeader handleException(Exception exception, RequestHeader requestHeader) throws Exception {
        exception.printStackTrace();
        // TODO Error Case에 따른 분류
        if (exception instanceof IOException) {

        }

        if (exception instanceof NullPointerException) {

        }

        // TODO Error page 동적 출력
        return new ExceptionResponseBuilder().build(requestHeader);
    }
}
