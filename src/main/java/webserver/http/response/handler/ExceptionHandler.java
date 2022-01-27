package webserver.http.response.handler;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_MOVED_TEMP;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import static webserver.http.HttpMeta.MIME_TYPE_OF_HTML;

import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.exceptions.NullRequestException;
import webserver.http.request.exceptions.PageNotFoundException;
import webserver.http.response.HttpResponse;

public class ExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    public static void handleException(HttpResponse response, Exception e) {
        log.error(e.getMessage());

        int statusCode = response.getStatusCode();
        if (statusCode == HTTP_OK || statusCode == HTTP_MOVED_TEMP) {
            log.error("An error occurred, but the Status code is not correct...");
            response.setStatusCode(HTTP_INTERNAL_ERROR);
        }

        if (e instanceof FileNotFoundException) {
            response.setStatusCode(HTTP_NOT_FOUND);
        }

        if (e instanceof PageNotFoundException) {
            response.setStatusCode(HTTP_NOT_FOUND);
        }

        if (e instanceof NullRequestException) {
            response.setStatusCode(HTTP_NOT_FOUND);
        }

        if (e instanceof AccessDeniedException) {
            response.setStatusCode(HTTP_FORBIDDEN);
        }

        response.setContentType(MIME_TYPE_OF_HTML);

        String message = "<html>" + "<head>" + "<title>" + "Server Error" + "</title>" + "</head>" +
                         "<body>" + "<h1>" + e.getMessage() + "</h1 >" +
                         "</body>" + "</html>";

        response.setMessage(message);
    }
}
