package webserver.http.response.body;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResponseBody {
    private final byte[] body;

    public ResponseBody(byte[] body) {
        this.body = body;
    }

    public static ResponseBody from(String url) throws IOException {
        return new ResponseBody(Files.readAllBytes(new File("./webapp" + url).toPath()));
    }

    public void write(DataOutputStream dos) throws IOException {
        dos.write(body, 0, body.length);
    }

    public int getBodyContentLength() {
        return body.length;
    }
}
