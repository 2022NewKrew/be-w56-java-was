package webserver.http.response;

import static webserver.http.HttpMeta.HTTP_STATUS_NOT_OK;
import static webserver.http.HttpMeta.HTTP_STATUS_OK;
import static webserver.http.HttpMeta.MIME_TYPE_OF_CSS;
import static webserver.http.HttpMeta.MIME_TYPE_OF_JAVASCRIPT;
import static webserver.http.HttpMeta.SUFFIX_OF_CSS_FILE;
import static webserver.http.HttpMeta.SUFFIX_OF_JS_FILE;
import static webserver.http.HttpMeta.VIEW_BASIC_PAGE;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HttpResponse {

    private final String httpVersion;
    private final HttpResponseHeaders headers;
    private final DataOutputStream dos;
    private int statusCode;
    private String status;

    public HttpResponse(String httpVersion, HttpResponseHeaders headers, DataOutputStream dos) {
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.dos = dos;
        this.statusCode = HttpURLConnection.HTTP_OK;
        this.status = HTTP_STATUS_OK;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        if (statusCode != HttpURLConnection.HTTP_OK && statusCode != HttpURLConnection.HTTP_MOVED_TEMP) {
            this.status = HTTP_STATUS_NOT_OK;
        }
    }

    public String getStatus() {
        return status;
    }

    public HttpResponseHeaders getHeaders() {
        return headers;
    }

    public void setHeader(HttpResponseHeader httpResponseHeader, String value) {
        headers.addHeader(httpResponseHeader, value);
    }

    public void setContentTypeWithFilePath(Path filePath) throws IOException {
        String contentType = getContentTypeFromFilePath(filePath);
        setContentType(contentType);
    }

    public void setContentType(String contentType) {
        setHeader(HttpResponseHeader.Content_Type, contentType);
    }

    private String getContentTypeFromFilePath(Path filePath) throws IOException {
        String fileName = filePath.getFileName().toString();
        if (fileName.endsWith(SUFFIX_OF_JS_FILE)) {
            return MIME_TYPE_OF_JAVASCRIPT;
        }
        if (fileName.endsWith(SUFFIX_OF_CSS_FILE)) {
            return MIME_TYPE_OF_CSS;
        }
        return Files.probeContentType(filePath);
    }

    public void setContentLength(long contentLength) {
        setHeader(HttpResponseHeader.Content_Length, Long.toString(contentLength));
    }

    public void setLocation(String redirectPath) {
        setHeader(HttpResponseHeader.Location, redirectPath);
    }

    public void sendNormalResponse(EncodedHttpResponse encodedResponse) throws IOException {
        buildOutputStreamOfHeaders(encodedResponse);
        dos.write(encodedResponse.getBody(), 0, encodedResponse.getBodyLength());
    }

    private void buildOutputStreamOfHeaders(EncodedHttpResponse encodedResponse) throws IOException {
        dos.writeBytes(encodedResponse.getStatusLine());
        List<String> responseHeader = encodedResponse.getResponseHeaders();
        for (String header : responseHeader) {
            dos.writeBytes(header);
        }
    }

    public void sendRedirectResponse(EncodedHttpResponse encodedResponse) throws IOException {
        buildOutputStreamOfHeaders(encodedResponse);
    }

    public void sendErrorResponse(EncodedHttpResponse encodedResponse, String message) throws IOException {
        buildOutputStreamOfHeaders(encodedResponse);
        dos.writeBytes(message);
    }

    public void redirect() {
        setStatusCode(HttpURLConnection.HTTP_MOVED_TEMP);
        setLocation(VIEW_BASIC_PAGE);
    }
}
