package webserver.response;

public class HttpResponseBody {

    private byte[] body;

    public HttpResponseBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBytes() {
        return body;
    }

    public int getLengthOfBody() {
        return body.length;
    }
}
