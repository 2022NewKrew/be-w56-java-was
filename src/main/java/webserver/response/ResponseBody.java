package webserver.response;

import java.util.Arrays;

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

    @Override
    public String toString() {
        return "ResponseBody{" +
                "body=" + Arrays.toString(body) +
                '}';
    }
}
