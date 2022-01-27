package cafe.controller.advice;

import cafe.controller.exception.IncorrectLoginUserException;
import framework.annotation.ControllerAdvice;
import framework.annotation.ExceptionHandler;
import framework.http.response.HttpResponse;
import framework.http.response.HttpResponseHeader;
import framework.http.enums.HttpStatus;
import framework.http.enums.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(values = IncorrectLoginUserException.class)
    public HttpResponse handleIncorrectLoginUserException(IncorrectLoginUserException incorrectLoginUserException) throws IOException {
        log.error(incorrectLoginUserException.getMessage());

        File file = new File("./webapp/user/login_failed.html");
        byte[] body = Files.readAllBytes(file.toPath());

        HttpResponseHeader responseHeader = new HttpResponseHeader();
        responseHeader.setContentType(MediaType.TEXT_HTML);
        responseHeader.setCookie("logined=fail; Path=/");
        responseHeader.setLocation("/user/login_failed.html");

        return new HttpResponse("HTTP/1.1", HttpStatus.FOUND, responseHeader, body);
    }

    @ExceptionHandler(values = IOException.class)
    public HttpResponse handleIOException(IOException ioException) {
        log.error(ioException.getMessage());
        return new HttpResponse("HTTP/1.1", HttpStatus.NOT_FOUND, new HttpResponseHeader());
    }
}
