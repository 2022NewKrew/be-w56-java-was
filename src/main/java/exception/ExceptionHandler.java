package exception;

import model.RequestHeader;
import util.Links;

import java.io.IOException;

public class ExceptionHandler {

    private ExceptionHandler() {
    }

    // Exception 처리
    public static String handleException(Exception exception, RequestHeader requestHeader) {
        exception.printStackTrace();
        // TODO Error Case에 따른 분류
        if (exception instanceof IOException) {
            // 발생 가능 경우 = readBody 도중 발생
            // 존재하지 않는 페이지, 404
        }

        if (exception instanceof NullPointerException) {

        }

        // Error Case에 따라 다른 오류 링크 리턴 OR 다이나믹으로 처리
        return Links.ERROR;
    }
}
