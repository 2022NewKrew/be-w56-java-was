package http.request;

import http.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

public class RequestBody {

    private byte[] body;

    private RequestBody(byte[] body) {
        this.body = body;
    }

    public static RequestBody create(BufferedReader br, Integer contentLength, Charset charset) throws IOException {
        return new RequestBody(IOUtils.readData(br, contentLength).getBytes(charset));
    }

    public String toString(Charset charset) {
        return URLDecoder.decode(new String(body), charset);
    }
}
