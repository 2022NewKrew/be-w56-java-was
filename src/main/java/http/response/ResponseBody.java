package http.response;

import http.request.RequestBody;

public class ResponseBody {

    private byte[] body;

    private ResponseBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }

    public static ResponseBody of(byte[] body) {
        return new ResponseBody(body);
    }
}
