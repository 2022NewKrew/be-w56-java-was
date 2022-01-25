package webserver.http.response;

import static webserver.http.HttpMeta.HTTP_STATUS_NOT_OK;
import static webserver.http.HttpMeta.HTTP_STATUS_OK;
import static webserver.http.HttpMeta.MIME_TYPE_OF_CSS;
import static webserver.http.HttpMeta.MIME_TYPE_OF_JAVASCRIPT;
import static webserver.http.HttpMeta.SUFFIX_OF_CSS_FILE;
import static webserver.http.HttpMeta.SUFFIX_OF_JS_FILE;

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
        if (statusCode != HttpURLConnection.HTTP_OK) {
            this.status = HTTP_STATUS_NOT_OK;
        }
    }

    public String getStatus() {
        return status;
    }

    public HttpResponseHeaders getHeaders() {
        return headers;
    }

    public void setHeader(Header header, String value) {
        headers.addHeader(header, value);
    }

    public void setContentType(Path filePath) throws IOException {
        String contentType = getContentTypeFromFilePath(filePath);
        setHeader(Header.Content_Type, contentType);
    }

    public void setContentLength(long contentLength) {
        setHeader(Header.Content_Length, Long.toString(contentLength));
    }

    public void setLocation(String redirectPath) {
        setHeader(Header.Location, redirectPath);
    }

    public void sendNormalResponse(EncodedHttpResponse encodedResponse) throws IOException {
        dos.writeBytes(encodedResponse.getStatusLine());
        List<String> responseHeader = encodedResponse.getResponseHeaders();
        for (String header : responseHeader) {
            dos.writeBytes(header);
        }
        dos.write(encodedResponse.getBody(), 0, encodedResponse.getBodyLength());
    }

    public void sendRedirectResponse(EncodedHttpResponse encodedResponse) throws IOException {
        dos.writeBytes(encodedResponse.getStatusLine());
        List<String> responseHeader = encodedResponse.getResponseHeaders();
        for (String header : responseHeader) {
            dos.writeBytes(header);
        }
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

    enum Header {
        Content_Type("Content-Type"),
        Content_Length("Content-Length"),
        Location("Location");

        private final String header;

        Header(String header) {
            this.header = header;
        }

        @Override
        public String toString() {
            return header;
        }
    }
}
