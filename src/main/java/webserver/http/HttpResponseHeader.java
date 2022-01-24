package webserver.http;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HttpResponseHeader {

    private List<String> lines = new ArrayList<>();

    public HttpResponseHeader(HttpResponseStatus status, int bodyContentLength) {
        buildHeaderLine(status, bodyContentLength);
    }

    private void buildHeaderLine(HttpResponseStatus status, int bodyLength) {
        lines.add("HTTP/1.1 " + status.getStatusNumber() + " " + status.getStatusMessage());
        lines.add("Date: " + LocalDateTime.now());
        lines.add("Content-Length: " + bodyLength);
        lines.add("Content-Type: text/html");
        lines = Collections.unmodifiableList(lines);
    }

    public List<String> getLines() {
        return lines;
    }
}
