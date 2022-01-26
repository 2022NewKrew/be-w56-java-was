package webserver.response;

public class HttpResponseBody {

    private byte[] body;

    public HttpResponseBody() {
        this.body = new byte[]{};
    }

    public HttpResponseBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBytes() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public int getLengthOfBody() {
        return body.length;
    }
}
