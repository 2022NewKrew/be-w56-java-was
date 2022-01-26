package http.response;

import http.common.HttpHeaders;
import http.common.HttpVersion;
import http.request.HttpRequest;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

public class HttpResponse {
    private static final String HTTP_RESPONSE_STATUS_LINE_DELIMITER = " ";
    private static final String HTTP_RESPONSE_LINE_DELIMITER = "\r\n";

    private final HttpVersion httpVersion;
    private final HttpStatus httpStatus;
    private final HttpHeaders httpHeaders;
    private final HttpResponseBody httpResponseBody;

    private HttpResponse(HttpVersion httpVersion, HttpStatus httpStatus, HttpHeaders httpHeaders, HttpResponseBody httpResponseBody) {
        this.httpVersion = httpVersion;
        this.httpStatus = httpStatus;
        this.httpHeaders = httpHeaders;
        this.httpResponseBody = httpResponseBody;
    }

    public static HttpResponse of(HttpRequest request, String viewName) throws IOException {
        HttpVersion version = request.getHttpVersion();
        HttpStatus status;
        byte[] viewByteArray;
        try {
            viewByteArray = Files.readAllBytes(new File("./webapp" + viewName).toPath());
            status = HttpStatus.OK;
        } catch (NoSuchFileException e) {
            viewName = "/error.html";
            viewByteArray = Files.readAllBytes(new File("./webapp" + viewName).toPath());
            status = HttpStatus.NOT_FOUND;
        }
        HttpResponseBody body = HttpResponseBody.of(viewByteArray, viewName);
        HttpHeaders httpHeaders = HttpHeaders.from(body);

        return new HttpResponse(version, status, httpHeaders, body);
    }

    public void send(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        writeStatusLine(dos);
        writeHeaders(dos);
        writeEmptyLine(dos);
        writeBody(dos);

        dos.flush();
    }

    private void writeStatusLine(DataOutputStream dos) throws IOException {
        dos.writeBytes(String.format("%s%s%d%s%s%s",
                httpVersion,
                HTTP_RESPONSE_STATUS_LINE_DELIMITER,
                httpStatus.getCode(),
                HTTP_RESPONSE_STATUS_LINE_DELIMITER,
                httpStatus.getText(),
                HTTP_RESPONSE_LINE_DELIMITER));
    }

    private void writeHeaders(DataOutputStream dos) throws IOException {
        dos.writeBytes(httpHeaders.getFormattedHeader());
    }

    private void writeBody(DataOutputStream dos) throws IOException {
        dos.write(httpResponseBody.getContent(), 0, httpResponseBody.getLength());
    }

    private void writeEmptyLine(DataOutputStream dos) throws  IOException {
        dos.writeBytes(HTTP_RESPONSE_LINE_DELIMITER);
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "httpVersion=" + httpVersion +
                ", httpStatus=" + httpStatus +
                ", httpHeaders=" + httpHeaders +
                ", httpResponseBody=" + httpResponseBody +
                '}';
    }
}
