package webserver;

import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpStatus;
import util.MIME;

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
    private static final Map<String, RequestMappingPath> requestMap;

    static {
        requestMap = new HashMap<>();
        for (RequestMappingPath value : RequestMappingPath.values()) {
            requestMap.put(value.getPath(), value);
        }
    }

    public static Response createResponse(Request request, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        String path = request.uri().getPath();

        boolean isRequestStaticResource = Arrays.stream(MIME.values())
                .anyMatch(m -> m.isExtensionMatch(path));

        if (isRequestStaticResource) {
            return responseStaticFile(dos, path);
        }

        if (!requestMap.containsKey(path)) {
            return response404NotFound(dos);
        }
        try {
            return requestMap.get(path).handle(request, dos);
        } catch (IllegalArgumentException e) {
            return response400BadRequest(dos, e);
        } catch (Exception e) {
            log.error(e.getMessage());
            return response500InternalServerError(dos);
        }
    }
    private static Response responseStaticFile(DataOutputStream dos, String path) {
        try{
            return new Response.Builder(dos)
                    .body(Files.readAllBytes(new File("./webapp" + path).toPath()))
                    .status(HttpStatus.OK)
                    .contentType(MIME.parse(path))
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            return response500InternalServerError(dos);
        }
    }
    private static Response response404NotFound(DataOutputStream dos) {
        return new Response.Builder(dos)
                .body(HttpStatus.NOT_FOUND.toString().getBytes())
                .status(HttpStatus.NOT_FOUND)
                .build();
    }
    private static Response response400BadRequest(DataOutputStream dos, Exception e) {
        return new Response.Builder(dos)
                .body((HttpStatus.BAD_REQUEST + ":" + e.getMessage()).getBytes())
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }
    private static Response response500InternalServerError(DataOutputStream dos) {
        log.info("response500InternalServerError");
        return new Response.Builder(dos)
                .body(HttpStatus.INTERNAL_SERVER_ERROR.toString().getBytes())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }
}
