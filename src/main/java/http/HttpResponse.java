package http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class HttpResponse {

    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String LINE_BREAK = "\r\n";

    private final HttpVersion version;
    private HttpStatus status;
    private final Map<HttpHeader, String> headers;
    private String view;
    private byte[] body;

    public HttpResponse(HttpVersion version, HttpStatus status,
        Map<HttpHeader, String> headers, String view, byte[] body) {
        this.version = version;
        this.status = status;
        this.headers = headers;
        this.view = view;
        this.body = body;
    }

    public void putHeader(HttpHeader header, String value) {
        headers.put(header, value);
    }

    public void writeBody(byte[] body) {
        this.body = body;
        putHeader(HttpHeader.CONTENT_LENGTH, String.valueOf(body.length));
    }

    public String getView() {
        return view;
    }

    public void write(DataOutputStream dos) throws IOException {
        dos.writeBytes(statusLine());
        dos.writeBytes(header());
        dos.writeBytes(LINE_BREAK);
        dos.write(body);
    }

    public void changeView(String view) {
        if (view.startsWith(REDIRECT_PREFIX)) {
            view = view.substring(REDIRECT_PREFIX.length());
            status = HttpStatus.FOUND;
            headers.put(HttpHeader.LOCATION, "http://" + headers.get(HttpHeader.HOST) + view);
        }
        this.view = view;
    }

    private String statusLine() {
        return version.toString() + " " + status.toString() + LINE_BREAK;
    }

    private String header() {
        StringBuilder sb = new StringBuilder();
        for (Entry<HttpHeader, String> entry : headers.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(LINE_BREAK);
        }
        return sb.toString();
    }
}
