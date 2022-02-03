package framework.advice;

import framework.annotation.ExceptionHandler;
import framework.http.response.HttpResponse;
import framework.http.response.HttpResponseHeader;
import framework.http.response.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FrameWorkExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(FrameWorkExceptionHandler.class);

    @ExceptionHandler(values = IOException.class)
    public HttpResponse handleIOException(IOException ioException) {
        log.error(ioException.getMessage());
        return new HttpResponse(HttpStatus.NOT_FOUND, new HttpResponseHeader());
    }
}
