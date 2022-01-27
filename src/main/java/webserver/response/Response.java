package webserver.response;

public class Response {
    private final StatusLine statusLine;
    private final ResponseHeader responseHeader;
    private final ResponseBody responseBody;

    private Response(StatusLine statusLine, ResponseHeader responseHeader, ResponseBody responseBody) {
        this.statusLine = statusLine;
        this.responseHeader = responseHeader;
        this.responseBody = responseBody;
    }

    public static Response of(StatusLine statusLine, ResponseHeader responseHeader, ResponseBody responseBody) {
        return new Response(statusLine, responseHeader, responseBody);
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public ResponseBody getResponseBody() {
        return responseBody;
    }

    @Override
    public String toString() {
        return "Response{" +
                "statusLine=" + statusLine +
                ", responseHeader=" + responseHeader +
                ", responseBody=" + responseBody +
                '}';
    }
}
