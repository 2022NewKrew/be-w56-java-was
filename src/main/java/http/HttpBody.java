package http;

import java.net.URLDecoder;
import java.nio.charset.Charset;

public class HttpBody {

    private byte[] body;

    private HttpBody(byte[] body) {
        this.body = body;
    }

    public static HttpBody of(byte[] body) {
        return new HttpBody(body);
    }

    public String toString(Charset charset) {
        return URLDecoder.decode(new String(body), charset);
    }

    public byte[] getBody() {
        return body;
    }
}
