package controller;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpStatus;
import util.request.Request;
import util.response.Response;
import util.response.ResponseBuilder;
import util.response.ResponseException;
import webserver.RequestHandler;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StaticController extends Controller{
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final StaticController staticController = new StaticController();

    public static StaticController getController() {
        return staticController;
    }
    @Override
    public Response view(Request request, String url) throws IOException {
        File file = new File("./webapp" + url);
        Path path = file.toPath();
        try {
            byte[] body = Files.readAllBytes(path);
            String contentType = new Tika().detect(file);
            log.debug("static url : {}, content type : {}, file : {}", url, contentType, path.getFileName());
            return new ResponseBuilder().setHttpStatus(HttpStatus.OK)
                    .addHeader("Content-Type",contentType + ";charset=utf-8")
                    .addHeader("Content-Length", String.valueOf(body.length))
                    .setBody(body)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            // 404 Not Found
            return ResponseException.notFoundResponse();
        }
    }
}
