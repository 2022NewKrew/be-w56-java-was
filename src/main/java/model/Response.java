package model;

import java.util.ArrayList;
import java.util.List;

public class Response {
    private static final String NEW_LINE = "\r\n";
    private static final String CONTENT_TYPE = "Content-Type: ";
    private static final String CONTENT_LENGTH = "Content-Length: ";
    private static final String CHARSET = ";charset=utf-8";
    private static final String LOCATION = "Location: ";
    private static final String COOKIE_FORMAT = "Set-Cookie: %s; Path=/";

    private final List<String> header;
    private HttpStatus httpStatus;
    private byte[] responseBody;

    public Response() {
        header = new ArrayList<>();
    }

    public void set200Header(Request request, byte[] responseBody) {
        header.add(CONTENT_TYPE + request.getAcceptType() + CHARSET);
        header.add(CONTENT_LENGTH + responseBody.length);
        httpStatus = HttpStatus.OK;
        this.responseBody = responseBody;
    }

    public void set302Header(String url) {
        header.add(LOCATION + url);
        httpStatus = HttpStatus.FOUND;
    }

    public String getHeaderMessage() {
        return String.join(NEW_LINE,
                String.format("HTTP/1.1 %d %s", httpStatus.getStatus(), httpStatus.getStatusMessage()),
                String.join(NEW_LINE, header),
                NEW_LINE);
    }

    public void setCookie(String value) {
        header.add(String.format(COOKIE_FORMAT, value));
    }

    public byte[] getResponseBody() {
        return responseBody;
    }
}
