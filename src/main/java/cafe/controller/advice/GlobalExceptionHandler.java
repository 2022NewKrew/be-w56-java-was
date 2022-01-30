package cafe.controller.advice;

import cafe.controller.exception.IncorrectLoginUserException;
import framework.annotation.ControllerAdvice;
import framework.annotation.ExceptionHandler;
import framework.http.enums.MediaType;
import framework.http.response.HttpResponse;
import framework.http.response.HttpResponseHeader;
import framework.http.response.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(values = IncorrectLoginUserException.class)
    public HttpResponse handleIncorrectLoginUserException(IncorrectLoginUserException incorrectLoginUserException) throws IOException {
        log.error(incorrectLoginUserException.getMessage());

        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);
        responseHeader.setCookie("logined=false; Path=/");
        responseHeader.setLocation("/user/login_failed.html");

        return new HttpResponse(HttpStatus.FOUND, responseHeader, "/user/login_failed.html");
    }

    @ExceptionHandler(values = IOException.class)
    public HttpResponse handleIOException(IOException ioException) {
        log.error(ioException.getMessage());
        return new HttpResponse(HttpStatus.NOT_FOUND, new HttpResponseHeader());
    }
}
