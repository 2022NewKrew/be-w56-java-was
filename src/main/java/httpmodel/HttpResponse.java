package httpmodel;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HttpResponse {

    private static final String NEW_LINE = "\r\n";
    private static final String CONTENT_LENGTH = "Content-Length: ";

    private final List<String> header = new ArrayList<>();
    private byte[] body;
    private HttpStatus httpStatus = HttpStatus.OK;

    public void setErrorResponse(String errorMessage, HttpStatus httpStatus) {
        addHeader("Content-Type", "application/json");
        body = errorMessage.getBytes(StandardCharsets.UTF_8);
        this.httpStatus = httpStatus;
    }

    public void set200OK(HttpRequest httpRequest, byte[] responseBody) {
        header.add("Content-Type: " + httpRequest.acceptType() + ";charset=utf-8");
        header.add(CONTENT_LENGTH + responseBody.length);
        httpStatus = HttpStatus.OK;
        body = responseBody;
    }

    public void set302Found(String location) {
        header.add("Location: " + location);
        httpStatus = HttpStatus.FOUND;
    }

    public void addHeader(String key, String value) {
        header.add(String.format("%s: %s", key, value));
    }

    public void addCookie(HttpRequest httpRequest) {
        if (Objects.isNull(httpRequest.getCookie("JSESSIONID"))) {
            addHeader("Set-Cookie", "JSESSIONID=" + httpRequest.getHttpSession().getId());
        }
    }

    public String message() {
        return String.join(NEW_LINE,
            String.format("HTTP/1.1 %d %s", httpStatus.getStatus(), httpStatus.getStatusMessage()),
            String.join(NEW_LINE, header),
            NEW_LINE
        );
    }

    public byte[] getBody() {
        return body;
    }
}
