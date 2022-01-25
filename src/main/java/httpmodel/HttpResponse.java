package httpmodel;

import java.util.ArrayList;
import java.util.List;

public class HttpResponse {

    private static final String NEW_LINE = "\r\n";
    private static final String CONTENT_LENGTH = "Content-Length: ";

    private final List<String> header = new ArrayList<>();
    private String body = "";
    private HttpStatus httpStatus = HttpStatus.OK;

    public void setErrorResponse(String errorMessage, HttpStatus httpStatus) {
        addHeader("Content-Type", "application/json");
        body = errorMessage;
        this.httpStatus = httpStatus;
    }

    public void set200OK(HttpRequest httpRequest, String responseBody) {
        header.add("Content-Type: " + httpRequest.acceptType() + ";charset=utf-8");
        header.add(CONTENT_LENGTH + responseBody.getBytes().length);
        httpStatus = HttpStatus.OK;
        body = responseBody;
    }

    public void addHeader(String key, String value) {
        header.add(String.format("%s: %s", key, value));
    }

    public String message() {
        return String.join(NEW_LINE,
            String.format("HTTP/1.1 %d %s", httpStatus.getStatus(), httpStatus.getStatusMessage()),
            String.join(NEW_LINE, header),
            "",
            body
        );
    }
}
