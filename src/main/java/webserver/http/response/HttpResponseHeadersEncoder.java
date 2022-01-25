package webserver.http.response;

import static webserver.http.HttpMeta.SEPARATOR_OF_BETWEEN_HEADERS;
import static webserver.http.HttpMeta.SEPARATOR_OF_HEADER_LINE;
import static webserver.http.HttpMeta.SEPARATOR_OF_STATUS_LINE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import webserver.http.response.HttpResponse.Header;

public class HttpResponseHeadersEncoder {

    public static EncodedHttpResponse encode(HttpResponse response, Path filePath) throws IOException {
        String responseStatusLine = getResponseStatusLine(
            response.getHttpVersion(),
            response.getStatusCode(),
            response.getStatus()
        );

        HttpResponseHeaders httpResponseHeaders = response.getHeaders();
        List<String> responseHeaders = new ArrayList<>();
        for (Header header : httpResponseHeaders.keySet()) {
            responseHeaders.add(getResponseHeaderLine(header, httpResponseHeaders.getValue(header)));
        }
        responseHeaders.add(SEPARATOR_OF_BETWEEN_HEADERS);

        byte[] body = null;
        if(filePath != null) {
            body = Files.readAllBytes(filePath);
        }

        return new EncodedHttpResponse(responseStatusLine, responseHeaders, body);
    }

    private static String getResponseStatusLine(String httpVersion, int statusCode, String status) {
        return httpVersion + SEPARATOR_OF_STATUS_LINE + statusCode + SEPARATOR_OF_STATUS_LINE + status +
               SEPARATOR_OF_BETWEEN_HEADERS;
    }

    private static String getResponseHeaderLine(Header header, String value) {
        return header + SEPARATOR_OF_HEADER_LINE + value + SEPARATOR_OF_BETWEEN_HEADERS;
    }
}
