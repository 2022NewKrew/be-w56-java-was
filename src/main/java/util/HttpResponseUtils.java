package util;

import controller.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by melodist
 * Date: 2022-01-27 027
 * Time: 오후 4:32
 */
public class HttpResponseUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpResponseUtils.class);
    private static final String CRLF = "\r\n";

    public static String createResponseString(Response response) {
        StringBuilder responseStringBuilder = new StringBuilder();
        if (response.getHttpStatus().equals(HttpStatus.OK)) {
            responseStringBuilder.append("HTTP/1.1 200 OK");
        }

        if (response.getHttpStatus().equals(HttpStatus.REDIRECT)) {
            responseStringBuilder.append("HTTP/1.1 302 FOUND");
        }

        if (response.getHttpStatus().equals(HttpStatus.NOT_FOUND)) {
            responseStringBuilder.append("HTTP/1.1 404 NOT FOUND");
        }

        responseStringBuilder.append(CRLF);

        if (response.getResponseHeader() != null) {
            responseStringBuilder.append(createHeaderString(response.getResponseHeader()))
                    .append(CRLF);
        }

        log.debug(responseStringBuilder.toString());

        return responseStringBuilder.toString();
    }

    private static String createHeaderString(Map<String, String> headers) {
        StringBuilder headersStringBuilder = new StringBuilder();
        headers.forEach((k, v) -> headersStringBuilder.append(k).append(": ").append(v).append(CRLF));
        return headersStringBuilder.toString();
    }
}
