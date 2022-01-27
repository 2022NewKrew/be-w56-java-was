package controller;

import http.ContentType;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.HttpStatus;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StaticController extends AbstractController {

    private static final String PATH = "/";
    private static final String PATHNAME = "./webapp";

    private static StaticController instance;

    private StaticController() {
    }

    public static StaticController getInstance() {
        if (instance == null) {
            instance = new StaticController();
        }
        return instance;
    }

    @Override
    protected HttpResponse doGet(HttpRequest request) throws IOException {
        Path path = new File(PATHNAME + request.getPath()).toPath();
        ContentType contentType = ContentType.match(request.getPath());
        return HttpResponse.ok(contentType, Files.readAllBytes(path));
    }

    @Override
    protected HttpResponse doPost(HttpRequest request) {
        return HttpResponse.error(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    public boolean match(String path) {
        return PATH.equals(path);
    }
}
