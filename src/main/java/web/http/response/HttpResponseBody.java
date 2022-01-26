package web.http.response;


public class HttpResponseBody {
    private final byte[] body;

    public HttpResponseBody(byte[] body) {
        this.body = body;
    }

    public HttpResponseBody()    {
        this.body = new byte[]{};
    }

    public byte[] getBody() {
        return body;
    }
    public int getBodyLength(){
        return body.length;
    }
}
