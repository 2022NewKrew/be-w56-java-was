package webserver;

import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class RequestMapper {
    private static final Logger log = LoggerFactory.getLogger(RequestMapper.class);
    private static final Map<String, RequestMappingPath> requestMap;

    static {
        requestMap = new HashMap<String, RequestMappingPath>();
        for (RequestMappingPath value : RequestMappingPath.values()) {
            requestMap.put(value.getPath(), value);
        }
        log.info("requestMap : "+requestMap);
    }

    public static Response createResponse(Request request, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        String path = request.uri().getPath();

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

    private static Response response404NotFound(DataOutputStream dos) {
        byte[] body = HttpStatus.NOT_FOUND.toString().getBytes();
        Map<String, String> headers = new HashMap<>();
        return new Response(dos, body, "", HttpStatus.NOT_FOUND, headers, null);
    }
    private static Response response400BadRequest(DataOutputStream dos, Exception e) {
        byte[] body = (HttpStatus.BAD_REQUEST + ":" + e.getMessage()).getBytes();
        Map<String, String> headers = new HashMap<>();
        return new Response(dos, body, "", HttpStatus.BAD_REQUEST, headers, null);
    }
    private static Response response500InternalServerError(DataOutputStream dos) {
        byte[] body = HttpStatus.INTERNAL_SERVER_ERROR.toString().getBytes();
        Map<String, String> headers = new HashMap<>();
        return new Response(dos, body, "", HttpStatus.INTERNAL_SERVER_ERROR, headers, null);
    }


}
