package webserver;

import Controller.*;
import config.config;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.enums.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class RequestMapper {
    private static final Logger logger = LoggerFactory.getLogger(RequestMapper.class);
    private static DataOutputStream dos;
    private static final Map<String, Map<HttpMethod, Controller>> apiMap;

    static {
        apiMap = new HashMap<>();
        Stream.of(UserController.values()).forEach(RequestMapper::addApi);
        Stream.of(MainController.values()).forEach(RequestMapper::addApi);
    }

    private static void addApi(Controller controller) {
        Map<HttpMethod, Controller> temp = new HashMap<>();
        temp.put(controller.getMethod(), controller);
        RequestMapper.apiMap.put(controller.getUrl(), temp);
    }

    public static MyHttpResponse createResponse(MyHttpRequest request, OutputStream out) {
        dos = new DataOutputStream(out);

        HttpMethod method = request.getMethod();
        String uri = request.getUri();
        logger.debug("method :{} / uri :{}", method, uri);

        try {
            if (!MIME.parse(uri).isNull()) {
                return staticResponse(request);
            }
            return apiMap.get(uri).get(method).run(request, dos);
        } catch (NullPointerException e) {
            return response404NotFound(request);
        } catch (IllegalArgumentException e) {
            return response400BadRequest(request);
        } catch (IOException e) {
            return response500InternalServerError(request);
        }
    }

    private static MyHttpResponse staticResponse(MyHttpRequest request) throws IOException {
        return new MyHttpResponse.Builder(dos)
                .setContentType(MIME.parse(request.getUri()).getContentType())
                .setBody(Files.readAllBytes(new File(config.DEFAULT_PATH + request.getUri()).toPath()))
                .build();
    }

    private static MyHttpResponse response400BadRequest(MyHttpRequest request) {
        return new MyHttpResponse.Builder(dos)
                .setCookie(request.getHeader("Cookie"))
                .setContentType(HttpStatus.BAD_REQUEST.toString())
                .setBody(HttpStatus.BAD_REQUEST.toString().getBytes())
                .build();
    }

    private static MyHttpResponse response404NotFound(MyHttpRequest request) {
        return new MyHttpResponse.Builder(dos)
                .setCookie(request.getHeader("Cookie"))
                .setContentType(HttpStatus.NOT_FOUND.toString())
                .setBody(HttpStatus.NOT_FOUND.toString().getBytes())
                .build();
    }

    private static MyHttpResponse response500InternalServerError(MyHttpRequest request) {
        return new MyHttpResponse.Builder(dos)
                .setCookie(request.getHeader("Cookie"))
                .setContentType(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .setBody(HttpStatus.INTERNAL_SERVER_ERROR.toString().getBytes())
                .build();
    }
}
