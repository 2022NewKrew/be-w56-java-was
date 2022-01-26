package controller;

import http.HttpRequest;
import http.HttpResponse;
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

        // TODO: 파일에 맞는 contentType 으로 변경 하기
        return HttpResponse.ok("*/*", Files.readAllBytes(path));
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
