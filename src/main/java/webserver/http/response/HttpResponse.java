package webserver.http.response;

public class HttpResponse {

    private final String header;
    private final byte[] body;

    public HttpResponse(String header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }

    public int bodyLength() {
        return body.length;
    }
}
