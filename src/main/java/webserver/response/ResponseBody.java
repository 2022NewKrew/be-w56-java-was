package webserver.response;

public class ResponseBody {
    private final byte[] body;

    private ResponseBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }

    public static ResponseBody from(byte[] body) {
        return new ResponseBody(body);
    }
}
