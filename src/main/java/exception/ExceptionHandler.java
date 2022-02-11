package exception;

import dynamic.DynamicHtmlBuilder;
import model.HttpResponseBuilder;
import model.RequestHeader;
import model.HttpResponse;
import model.builder.ExceptionResponseBuilder;
import util.HttpResponseHeader;
import util.Links;

import java.io.IOException;

public class ExceptionHandler {

    private ExceptionHandler() {
    }

    // Exception 처리
    public static void handleException(Exception exception, RequestHeader requestHeader) throws Exception {
        exception.printStackTrace();
        // TODO Error Case에 따른 분류
        if (exception instanceof IOException) {

        }

        if (exception instanceof NullPointerException) {

        }
    }
}
