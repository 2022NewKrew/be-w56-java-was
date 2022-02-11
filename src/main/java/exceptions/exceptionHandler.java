package exceptions;

import java.io.IOException;
import java.io.OutputStream;
import model.HttpClientErrorResponse;
import model.HttpResponse;
import model.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.View;

public class exceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(exceptionHandler.class);

    public static void httpMethodNotFound(OutputStream out, String message) throws IOException {
        log.error(message);
        HttpResponse httpResponse = HttpClientErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED,
                View.staticFile("/errors/methodNotAllowed.html"));
        View.sendResponse(out, httpResponse.message());
    }

    public static void badRequestFormat(OutputStream out, String message) throws IOException {
        log.error(message);
        HttpResponse httpResponse = HttpClientErrorResponse.of(HttpStatus.BAD_REQUEST,
                View.staticFile("/errors/badRequest.html"));
        View.sendResponse(out, httpResponse.message());
    }

    public static void loginFailed(OutputStream out, String message) throws IOException {
        log.error(message);
        HttpResponse httpResponse = HttpClientErrorResponse.of(HttpStatus.UNAUTHORIZED,
                View.staticFile("/errors/loginFailed.html"));
        View.sendResponse(out, httpResponse.message());
    }

    public static void logoutFailed(OutputStream out, String message) throws IOException {
        log.error(message);
        HttpResponse httpResponse = HttpClientErrorResponse.of(HttpStatus.UNAUTHORIZED,
                View.staticFile("/errors/logoutFailed.html"));
        View.sendResponse(out, httpResponse.message());
    }
}
