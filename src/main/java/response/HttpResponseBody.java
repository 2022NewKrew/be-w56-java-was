package response;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HttpResponseBody {
    private static final String STATIC_ROOT = "./webapp";
    private final byte[] body;

    public HttpResponseBody(byte[] body) {
        this.body = body;
    }

    public static HttpResponseBody createFromUrl(String url) throws IOException {
        File file = new File(STATIC_ROOT + url);
        return new HttpResponseBody(Files.readAllBytes(file.toPath()));
    }

    public void writeToDataOutputStream(DataOutputStream dos) throws IOException {
        try {
            dos.write(body);
        } catch (IOException e) {
            throw new IOException("Failed to write response body to dos");
        }
    }

    public int length() { return body.length; }
}
