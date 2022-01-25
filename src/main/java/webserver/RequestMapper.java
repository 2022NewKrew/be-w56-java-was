package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpStatus;
import webserver.http.MIME;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestMapper {

    private static final Logger log = LoggerFactory.getLogger(RequestMapper.class);
    private static final Map<String, RequestMappingInfo> requestMap;

    static {
        requestMap = new HashMap<>();
        for (RequestMappingInfo value : RequestMappingInfo.values()) {
            requestMap.put(value.getPath(), value);
        }
    }

    public static MyHttpResponse process(MyHttpRequest in, OutputStream out) {
        String path = in.uri().getPath();
        DataOutputStream dos = new DataOutputStream(out);

        boolean isRequestStaticResource = Arrays.stream(MIME.values())
                .anyMatch(m -> m.isExtensionMatch(path));

        if (isRequestStaticResource) {
            return responseStaticResource(dos, path);
        }

        if (!requestMap.containsKey(path)) {
            return response404NotFound(dos);
        }
        try {
            return requestMap.get(path).handle(in, dos);
        } catch (IllegalArgumentException e) {
            return response400BadRequest(dos, e);
        } catch (Exception e) {
            log.error(e.getMessage());
            return response500InternalServerError(dos);
        }
    }

    private static MyHttpResponse responseStaticResource(DataOutputStream dos, String path) {
        try {
            byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());

            return MyHttpResponse.builder(dos)
                    .status(HttpStatus.OK)
                    .contentType(MIME.parse(path))
                    .body(body)
                    .build();
        } catch (IOException e) {
            log.error(e.getMessage());
            return response500InternalServerError(dos);
        }
    }

    private static MyHttpResponse response404NotFound(DataOutputStream dos) {
        byte[] body = "404 - NOT FOUND!".getBytes();

        return MyHttpResponse.builder(dos)
                .status(HttpStatus.NOT_FOUND)
                .body(body)
                .build();
    }

    private static MyHttpResponse response400BadRequest(DataOutputStream dos, Exception e) {
        byte[] body = ("400 - BAD REQUEST! - " + e.getMessage()).getBytes();

        return MyHttpResponse.builder(dos)
                .status(HttpStatus.BAD_REQUEST)
                .body(body)
                .build();
    }

    private static MyHttpResponse response500InternalServerError(DataOutputStream dos) {
        byte[] body = "500 - INTERNAL SERVER ERROR!".getBytes();

        return MyHttpResponse.builder(dos)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body)
                .build();
    }
}
