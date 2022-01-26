package infrastructure.util;

import infrastructure.model.*;

import java.io.IOException;
import java.util.stream.Collectors;

import static infrastructure.config.ServerConfig.DELIMITER;
import static infrastructure.config.ServerConfig.SUPPORTEDVERSION;

public class HttpResponseUtils {

    private static final String EMPTY_LINE = "\r\n";

    public static String convertResponseStartLine(ResponseLine responseLine) {
        return SUPPORTEDVERSION + DELIMITER
                + responseLine.getHttpStatus().getStartLineMessage()
                + EMPTY_LINE;
    }

    public static String convertHeader(HttpHeader httpHeader) {
        return httpHeader.getHeaders()
                .stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining(EMPTY_LINE));
    }

    public static HttpResponse notFound() throws IOException {
        return new HttpResponse(
                ResponseLine.valueOf(HttpStatus.NOT_FOUND),
                HttpHeader.of(Pair.of("Content-Type", ContentType.HTML.convertToResponse())),
                HttpBody.valueOfFile("/error.html")
        );
    }

    public static HttpResponse badRequest() throws IOException {
        return new HttpResponse(
                ResponseLine.valueOf(HttpStatus.BAD_REQUEST),
                HttpHeader.of(Pair.of("Content-Type", ContentType.HTML.convertToResponse())),
                HttpBody.valueOfFile("/error.html")
        );
    }
}
