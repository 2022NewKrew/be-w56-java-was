package webserver;

import static webserver.http.HttpMeta.MIME_TYPE_OF_HTML;

import java.io.IOException;
import java.net.HttpURLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.response.EncodedHttpResponse;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseHeadersEncoder;

public class ExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    public static void handleException(HttpResponse response, Exception e) throws IOException {
        log.error(e.getMessage());

        int statusCode = response.getStatusCode();
        if (statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_MOVED_TEMP) {
            log.error("An error occurred, but the Status code is not correct...");
            response.setStatusCode(HttpURLConnection.HTTP_INTERNAL_ERROR);
        }

        response.setContentType(MIME_TYPE_OF_HTML);

        EncodedHttpResponse encodedHttpResponse = HttpResponseHeadersEncoder.encode(response, null);

        String message = "<html>" + "<head>" + "<title>" + "Server Error" + "</title>" + "</head>" +
                         "<body>" + "<h1>" + e.getMessage() + "</h1 >" +
                         "</body>" + "</html>";

        response.sendErrorResponse(encodedHttpResponse, message);
    }
}
