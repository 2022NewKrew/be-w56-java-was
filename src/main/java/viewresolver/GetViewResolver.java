package viewresolver;

import dto.HttpResponseStatus;
import dto.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class GetViewResolver {
    private static final Logger log = LoggerFactory.getLogger(GetViewResolver.class);
    private static final GetViewResolver INSTANCE = new GetViewResolver();
    private static final String STATIC_FILE_BASE_DIRECTORY = "./webapp";

    private GetViewResolver() {}

    public static GetViewResolver getInstance() {
        return INSTANCE;
    }

    public void response(RequestInfo requestInfo, DataOutputStream dos) {
        String requestPath = requestInfo.getRequestPath();
        String version = requestInfo.getVersion();
        try {
            byte[] body = Files.readAllBytes(new File(STATIC_FILE_BASE_DIRECTORY + requestPath).toPath());
            this.responseHeader(dos, version, HttpResponseStatus.OK, body.length, new ArrayList<>());
            this.responseBody(dos, body);
        } catch(Exception e) {
            log.error("[ERROR] - {}", e.getMessage());
            this.errorResponse("/error.html", version, dos);
        }
    }

    public void errorResponse(String path, String version, DataOutputStream dos) {
        try {
            byte[] body = Files.readAllBytes(new File(STATIC_FILE_BASE_DIRECTORY + path).toPath());
            this.responseHeader(dos, version, HttpResponseStatus.NOT_FOUND, body.length, new ArrayList<>());
            this.responseBody(dos, body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void responseHeader(DataOutputStream dos, String version, HttpResponseStatus status, int lengthOfBodyContent, List<String> addedHeaders) {
        try {
            dos.writeBytes(String.format("%s %d %s \r\n", version, status.getStatusCode(), status.getMessage()));
//            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");

            for(String h: addedHeaders) {
                dos.writeBytes(h);
                dos.writeBytes("\r\n");
            }

            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
