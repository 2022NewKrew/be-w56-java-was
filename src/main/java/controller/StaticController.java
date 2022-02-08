package controller;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import webserver.http.HttpStatus;
import webserver.request.Request;
import webserver.response.Response;
import webserver.response.ResponseBuilder;
import webserver.response.ResponseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StaticController extends Controller {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final StaticController staticController = new StaticController();

    private StaticController() {
    }

    public static StaticController getInstance() {
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
                    .setContent(contentType, body)
                    .setBody(body)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            // 404 Not Found
            return ResponseException.notFoundResponse();
        }
    }
}
