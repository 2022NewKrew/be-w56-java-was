package webserver;

import lombok.extern.slf4j.Slf4j;
import webserver.model.WebHttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;

@Slf4j
public class ViewRenderer {

    private static final ViewRenderer INSTANCE = new ViewRenderer();

    private ViewRenderer() {
    }

    public static ViewRenderer getInstance() {
        return INSTANCE;
    }

    public void redirect(DataOutputStream dos, WebHttpResponse httpResponse) throws IOException {
        try {
            dos.writeBytes(httpResponse.toString());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseResource(DataOutputStream dos, WebHttpResponse httpResponse) throws IOException {
        try {
            dos.writeBytes(httpResponse.toString());
            responseBody(dos, httpResponse.getBody());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            StringBuilder sb = new StringBuilder();
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
