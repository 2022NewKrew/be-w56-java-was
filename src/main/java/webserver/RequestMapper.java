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

import static webserver.WebServer.DEFAULT_RESOURCES_DIR;

public class RequestMapper {

    private static final Logger log = LoggerFactory.getLogger(RequestMapper.class);

    public static MyHttpResponse process(MyHttpRequest request, OutputStream out) {
        String path = request.uri().getPath();
        DataOutputStream dos = new DataOutputStream(out);

        boolean isRequestStaticResource = Arrays.stream(MIME.values())
                .anyMatch(m -> m.isExtensionMatch(path));

        if (isRequestStaticResource) {
            return responseStaticResource(dos, path);
        }

        return RequestMappingInfo.handleRequest(request, dos, path);
    }

    private static MyHttpResponse responseStaticResource(DataOutputStream dos, String path) {
        try {
            byte[] body = Files.readAllBytes(new File(DEFAULT_RESOURCES_DIR + path).toPath());

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
        byte[] body = HttpStatus.NOT_FOUND.toString().getBytes();

        return MyHttpResponse.builder(dos)
                .status(HttpStatus.NOT_FOUND)
                .body(body)
                .build();
    }

    private static MyHttpResponse response400BadRequest(DataOutputStream dos, Exception e) {
        byte[] body = (HttpStatus.BAD_REQUEST + ":" + e.getMessage()).getBytes();

        return MyHttpResponse.builder(dos)
                .status(HttpStatus.BAD_REQUEST)
                .body(body)
                .build();
    }

    private static MyHttpResponse response500InternalServerError(DataOutputStream dos) {
        byte[] body = HttpStatus.INTERNAL_SERVER_ERROR.toString().getBytes();

        return MyHttpResponse.builder(dos)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body)
                .build();
    }
}
