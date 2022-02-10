package model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import util.HttpResponseHeader;

import java.io.DataOutputStream;
import java.io.IOException;

@Slf4j
@Data
@Builder
public class HttpResponse {
    private String locationUri;
    private String accept;
    private byte[] body;
    private HttpResponseHeader htmlResponseHeader;

    public int getLengthOfBodyContent() {
        return body.length;
    }

    public void responseBody(DataOutputStream dos) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
