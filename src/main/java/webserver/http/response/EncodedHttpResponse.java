package webserver.http.response;

import java.util.List;

public class EncodedHttpResponse {

    private final String statusLine;
    private final List<String> responseHeaders;
    private final byte[] body;

    public EncodedHttpResponse(String statusLine, List<String> responseHeaders, byte[] body) {
        this.statusLine = statusLine;
        this.responseHeaders = responseHeaders;
        this.body = body;
    }

    public String getStatusLine() {
        return statusLine;
    }

    public List<String> getResponseHeaders() {
        return responseHeaders;
    }

    public byte[] getBody() {
        return body;
    }

    public int getBodyLength() {
        return body.length;
    }
}
