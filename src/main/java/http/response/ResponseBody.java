package http.response;

public class ResponseBody {

    private byte[] body;

    public void updateBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }
}
