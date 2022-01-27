package infrastructure.util;

import infrastructure.config.ServerConfig;
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
        return httpHeader.getHeaders().entrySet()
                .stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining(EMPTY_LINE));
    }

    public static HttpResponse notFound() throws IOException {
        return HttpResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .setHeader("Content-Type", ContentType.HTML.convertToResponse())
                .body(HttpByteArrayBody.setFile("/error.html"))
                .build();
    }

    public static HttpResponse badRequest() throws IOException {
        return HttpResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .setHeader("Content-Type", ContentType.HTML.convertToResponse())
                .body(HttpByteArrayBody.setFile("/error.html"))
                .build();
    }

    public static HttpResponse found(String path) throws IOException {
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .setHeader("Location", ServerConfig.getAuthority() + path)
                .build();
    }
}
