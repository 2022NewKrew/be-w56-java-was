package http.response;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class HttpResponseBody {
    public static final String STATIC_ROOT = "./webapp";
    private final byte[] body;

    private HttpResponseBody(byte[] body) {
        this.body = body;
    }

    // body 가 비어있는 HttpResponseBody 객체를 만들어 반환하는 팩토리 메소드
    public static HttpResponseBody empty() {
        byte[] emptyBody = "".getBytes(StandardCharsets.UTF_8);
        return new HttpResponseBody(emptyBody);
    }

    public static HttpResponseBody createFromUrl(final String url) {
        File file = new File(STATIC_ROOT + url);
        try {
            return new HttpResponseBody(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpResponseBody createFromStringBuilder(final StringBuilder sb) {
        return new HttpResponseBody(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    public void writeToDataOutputStream(final DataOutputStream dos) throws IOException {
        try {
            dos.write(body);
        } catch (IOException e) {
            throw new IOException("Failed to write response body to dos");
        }
    }

    public int length() { return body.length; }
}
