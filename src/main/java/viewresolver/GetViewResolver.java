package viewresolver;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dto.HttpResponseStatus;
import dto.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class GetViewResolver {
    private static final Logger log = LoggerFactory.getLogger(GetViewResolver.class);
    private static final GetViewResolver INSTANCE = new GetViewResolver();
    private static final String STATIC_FILE_BASE_DIRECTORY = "./webapp";
    private static final Map<String, String> contentTypes;

    static {
        contentTypes = Map.of("text/css", "text/css", "text/javascript", "text/javascript");
    }

    private GetViewResolver() {}

    public static GetViewResolver getInstance() {
        return INSTANCE;
    }

    public void response(RequestInfo requestInfo, DataOutputStream dos) {
        String requestPath = requestInfo.getRequestPath();
        String version = requestInfo.getVersion();
        try {
            byte[] body = Files.readAllBytes(new File(STATIC_FILE_BASE_DIRECTORY + requestPath).toPath());
            log.info("headers : {}", requestInfo.getHeaders());
            String contentType = requestInfo.getHeaders().get("Accept");
            String responseContentType = contentTypes.get(contentType);
            List<String> addedHeaders = responseContentType == null ? Lists.newArrayList() : List.of("Content-Type: " + responseContentType);
            this.responseHeader(dos, version, HttpResponseStatus.OK, body.length, addedHeaders);
            this.responseBody(dos, body);
        } catch(Exception e) {
            log.error("[ERROR] - {}", e.getMessage());
            this.errorResponse("/error.html", version, dos);
        }
    }

    public void dynamicResponse(DataOutputStream dos, RequestInfo requestInfo, String userListTableTemplate) {
        String requestPath = requestInfo.getRequestPath();
        switch(requestPath) {
            case "/user/list":
                this.responseUserList(dos, requestInfo, userListTableTemplate);
                break;
        }
    }

    private void responseUserList(DataOutputStream dos, RequestInfo requestInfo, String userListTableTemplate) {
        String version = requestInfo.getVersion();

        try {
            String template = Files.readString(new File(STATIC_FILE_BASE_DIRECTORY + "/user/list.html").toPath());
            String[] tokens = template.split("<tbody>|</tbody>");
            StringBuilder sb = new StringBuilder();
            sb.append(tokens[0]).append(userListTableTemplate).append(tokens[2]);

            byte[] body = sb.toString().getBytes(StandardCharsets.UTF_8);
            this.responseHeader(dos, version, HttpResponseStatus.OK, body.length, new ArrayList<>());
            this.responseBody(dos, body);
        } catch (IOException e) {
            log.error("[ERROR] - {}", e.getMessage());
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
