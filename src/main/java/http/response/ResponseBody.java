package http.response;

public class ResponseBody {

    private final byte[] body;

    public ResponseBody(byte[] body) {
        this.body = body;
    }

    public static ResponseBody getEmptyBody() {
        return new ResponseBody(null);
    }

    public byte[] getBody() {
        return body;
    }

    public int getBodyLength() {
        return body.length;
    }
}
