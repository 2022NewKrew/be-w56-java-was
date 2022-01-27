package webserver.http.response.handler;

import static java.net.HttpURLConnection.HTTP_MOVED_TEMP;
import static java.net.HttpURLConnection.HTTP_OK;
import static webserver.http.HttpMeta.SEPARATOR_OF_BETWEEN_HEADERS;
import static webserver.http.HttpMeta.SEPARATOR_OF_HEADER_LINE;
import static webserver.http.HttpMeta.SEPARATOR_OF_STATUS_LINE;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseHeader;
import webserver.http.response.HttpResponseHeaders;

public class ResponseHandler {

    private final DataOutputStream dos;
    private final HttpResponse response;

    public ResponseHandler(DataOutputStream dos, HttpResponse response) {
        this.dos = dos;
        this.response = response;
    }

    private static String getResponseHeaderLine(HttpResponseHeader header, String value) {
        return header + SEPARATOR_OF_HEADER_LINE + value + SEPARATOR_OF_BETWEEN_HEADERS;
    }

    public void sendResponse() throws IOException {

        int statusCode = response.getStatusCode();
        switch (statusCode) {
            case HTTP_OK:
                sendNormalResponse();
                break;
            case HTTP_MOVED_TEMP:
                sendRedirectResponse();
                break;
            default:
                sendErrorResponse();
        }
    }

    private void sendNormalResponse() throws IOException {
        buildOutputStreamOfHeaders();
        byte[] body = getBody();
        dos.write(body, 0, body.length);
    }

    private byte[] getBody() throws IOException {
        byte[] body = null;
        String filePath = response.getViewPage();
        if (filePath != null) {
            body = Files.readAllBytes(Path.of(filePath));
        }
        return body;
    }

    private void buildOutputStreamOfHeaders() throws IOException {
        dos.writeBytes(getResponseStatusLine());
        List<String> responseHeader = getResponseHeaders();
        for (String header : responseHeader) {
            dos.writeBytes(header);
        }
    }

    private String getResponseStatusLine() {
        return response.getHttpVersion() + SEPARATOR_OF_STATUS_LINE + response.getStatusCode() +
               SEPARATOR_OF_STATUS_LINE + response.getStatus() + SEPARATOR_OF_BETWEEN_HEADERS;
    }

    private List<String> getResponseHeaders() {
        HttpResponseHeaders httpResponseHeaders = response.getHeaders();
        List<String> responseHeaders = new ArrayList<>();
        for (HttpResponseHeader header : httpResponseHeaders.keySet()) {
            responseHeaders.add(getResponseHeaderLine(header, httpResponseHeaders.getValue(header)));
        }
        responseHeaders.add(SEPARATOR_OF_BETWEEN_HEADERS);
        return responseHeaders;
    }

    private void sendRedirectResponse() throws IOException {
        buildOutputStreamOfHeaders();
    }

    public void sendErrorResponse() throws IOException {
        buildOutputStreamOfHeaders();
        dos.writeBytes(response.getMessage());
    }
}
