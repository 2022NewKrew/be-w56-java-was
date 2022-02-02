package http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponse {
    private byte[] header;
    private byte[] body;

    public static HttpResponse of(byte[] header, byte[] body) {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.header = header;
        httpResponse.body = body;
        return httpResponse;
    }

    public void write(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.write(header);
        dos.write(body);
        dos.flush();
    }
}
