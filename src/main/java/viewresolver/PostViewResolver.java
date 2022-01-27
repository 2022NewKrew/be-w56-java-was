package viewresolver;

import dto.HttpResponseStatus;
import dto.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostViewResolver {

    private static final Logger log = LoggerFactory.getLogger(PostViewResolver.class);
    private static final PostViewResolver INSTANCE = new PostViewResolver();

    private PostViewResolver() {}

    public static PostViewResolver getInstance() {
        return INSTANCE;
    }

    public void response(RequestInfo requestInfo, String redirectPath, DataOutputStream dos) {
        String addHeader = "Location: /index.html";
        this.responseHeader(dos, requestInfo.getVersion(), HttpResponseStatus.FOUND, List.of(addHeader));
    }

    private void responseHeader(DataOutputStream dos, String version, HttpResponseStatus status, List<String> addedHeaders) {
        try {
            dos.writeBytes(String.format("%s %d %s \r\n", version, status.getStatusCode(), status.getMessage()));
//            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");

            for(String h: addedHeaders) {
                dos.writeBytes(h);
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
