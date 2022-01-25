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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestMapper {

    private static final Logger log = LoggerFactory.getLogger(RequestMapper.class);
    private static final Map<String, RequestMappingInfo> requestMap;
    private static final List<String> STATIC_RESOURCE_DIR = List.of("/js", "/css", "/fonts", "/images", "/favicon.ico");

    static {
        requestMap = new HashMap<>();
        for (RequestMappingInfo value : RequestMappingInfo.values()) {
            requestMap.put(value.getPath(), value);
        }
    }

    public static void process(MyHttpRequest in, OutputStream out) {
        String path = in.uri().getPath();
        DataOutputStream dos = new DataOutputStream(out);

        boolean isRequestStaticResource = STATIC_RESOURCE_DIR.stream()
                .anyMatch(path::startsWith);

        if (isRequestStaticResource) {
            responseStaticResource(dos, path);
            return;
        }

        if (!requestMap.containsKey(path)) {
            response404NotFound(dos);
            return;
        }
        try {
            requestMap.get(path).handle(in, dos);
        } catch (Exception e) {
            response500InternalServerError(dos);
        }
    }

    private static void responseStaticResource(DataOutputStream dos, String path) {
        try {
            byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());

            MyHttpResponse response = MyHttpResponse.builder(dos)
                    .status(HttpStatus.OK)
                    .contentType(MIME.parse(path))
                    .body(body)
                    .build();

            response.writeBytes();
            response.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void response404NotFound(DataOutputStream dos) {
        byte[] body = "404 - NOT FOUND!".getBytes();

        MyHttpResponse response = MyHttpResponse.builder(dos)
                .status(HttpStatus.NOT_FOUND)
                .body(body)
                .build();

        response.writeBytes();
        response.flush();
    }

    private static void response500InternalServerError(DataOutputStream dos) {
        byte[] body = "500 - INTERNAL SERVER ERROR!".getBytes();

        MyHttpResponse response = MyHttpResponse.builder(dos)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body)
                .build();

        response.writeBytes();
        response.flush();
    }
}
