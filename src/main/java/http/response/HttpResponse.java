package http.response;

import handler.HandlerResult;
import http.common.Headers;
import http.common.HttpVersion;
import http.common.Mime;
import http.request.HttpRequest;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

public class HttpResponse {
    private static final String VIEW_FILE_ROOT_PATH = "./webapp";
    private static final String ERROR_VIEW_URI = "/error.html";

    private static final String HTTP_RESPONSE_STATUS_LINE_DELIMITER = " ";
    private static final String HTTP_RESPONSE_LINE_DELIMITER = "\r\n";

    private final HttpVersion httpVersion;
    private final Status status;
    private final Headers headers;
    private final ResponseBody responseBody;

    public HttpResponse(HttpVersion httpVersion, Status status, Headers headers, ResponseBody responseBody) {
        this.httpVersion = httpVersion;
        this.status = status;
        this.headers = headers;
        this.responseBody = responseBody;
    }

    public static HttpResponse ofStatic(HttpRequest request) throws IOException {
        HttpVersion httpVersion = request.getVersion();
        Status status;
        byte[] viewByteArray;
        String viewName = request.getUri().getValue();
        if("/".equals(viewName) || viewName.isBlank()) {
            viewName = "/index.html";
        }

        try {
            viewByteArray = Files.readAllBytes(new File(VIEW_FILE_ROOT_PATH + viewName).toPath());
            status = Status.OK;
        } catch (NoSuchFileException e) {
            viewName = ERROR_VIEW_URI;
            viewByteArray = Files.readAllBytes(new File(VIEW_FILE_ROOT_PATH + viewName).toPath());
            status = Status.NOT_FOUND;
        }
        ResponseBody body = ResponseBody.of(viewByteArray);
        Headers httpHeaders = Headers.withBodyOf(body.getLength(), Mime.fromFileName(viewName));

        return new HttpResponse(httpVersion, status, httpHeaders, body);
    }

    public static HttpResponse fromHandlerResult(HttpRequest httpRequest, HandlerResult handlerResult) throws IOException {
        HttpVersion httpVersion = httpRequest.getVersion();
        Status status;
        if (handlerResult.isRedirect()) {
            status = Status.FOUND;
            Headers httpHeaders = Headers.redirection(handlerResult.getUri());
            httpHeaders.addCookies(handlerResult.getCookies());
            return new HttpResponse(httpVersion, status, httpHeaders, ResponseBody.empty());
        }

        byte[] viewByteArray;
        String viewName = handlerResult.getUri();
        try {
            viewByteArray = Files.readAllBytes(new File(VIEW_FILE_ROOT_PATH + viewName).toPath());
            status = Status.OK;
        } catch (NoSuchFileException e) {
            viewName = ERROR_VIEW_URI;
            viewByteArray = Files.readAllBytes(new File(VIEW_FILE_ROOT_PATH + viewName).toPath());
            status = Status.NOT_FOUND;
        }
        ResponseBody body = ResponseBody.of(viewByteArray);
        Headers httpHeaders = Headers.withBodyOf(body.getLength(), Mime.fromFileName(viewName));

        return new HttpResponse(httpVersion, status, httpHeaders, body);
    }

    public static HttpResponse error() throws IOException {
        HttpVersion httpVersion = HttpVersion.HTTP_1_1;
        Status status = Status.INTERNAL_SERVER_ERROR;
        byte[] viewByteArray;

        String viewName = ERROR_VIEW_URI;
        viewByteArray = Files.readAllBytes(new File(VIEW_FILE_ROOT_PATH + viewName).toPath());

        ResponseBody body = ResponseBody.of(viewByteArray);
        Headers httpHeaders = Headers.withBodyOf(body.getLength(), Mime.fromFileName(viewName));

        return new HttpResponse(httpVersion, status, httpHeaders, body);
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
                status.getCode(),
                HTTP_RESPONSE_STATUS_LINE_DELIMITER,
                status.getText(),
                HTTP_RESPONSE_LINE_DELIMITER));
    }

    private void writeHeaders(DataOutputStream dos) throws IOException {
        dos.writeBytes(headers.getFormattedHeader());
    }

    private void writeBody(DataOutputStream dos) throws IOException {
        dos.write(responseBody.getContent(), 0, responseBody.getLength());
    }

    private void writeEmptyLine(DataOutputStream dos) throws  IOException {
        dos.writeBytes(HTTP_RESPONSE_LINE_DELIMITER);
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "version=" + httpVersion +
                ", status=" + status +
                ", headers=" + headers +
                ", responseBody=" + responseBody +
                '}';
    }
}
