package controller;

import annotation.RequestMapping;
import org.apache.tika.Tika;
import util.HttpMethod;
import util.HttpStatus;
import util.response.Response;
import util.response.ResponseBuilder;
import util.response.ResponseException;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class MainController extends Controller {

    private static final MainController mainController = new MainController();

    public static MainController getMainController() {
        return mainController;
    }

    @RequestMapping(method = HttpMethod.GET)
    private Response main(@Nullable Map<String, String> parameters) throws IOException {
        File file = new File("./webapp/index.html");
        Path path = file.toPath();
        try {
            byte[] body = Files.readAllBytes(path);
            String contentType = new Tika().detect(file);
            return new ResponseBuilder().setHttpStatus(HttpStatus.OK)
                    .addHeader("Content-Type", contentType + ";charset=utf-8")
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
