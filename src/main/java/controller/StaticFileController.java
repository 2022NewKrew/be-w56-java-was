package controller;

import java.io.File;
import java.io.IOException;
import model.HttpClientErrorResponse;
import model.HttpRequest;
import model.HttpResponse;
import model.HttpStatus;
import model.HttpSuccessfulResponse;

public class StaticFileController implements Controller {

    private static StaticFileController instance;

    public static synchronized StaticFileController getInstance() {
        if (instance == null) {
            instance = new StaticFileController();
        }
        return instance;
    }

    @Override
    public HttpResponse run(HttpRequest request) throws IOException {
        File file = new File("./webapp" + request.getUrl());
        if (!file.exists()) {
            return HttpClientErrorResponse.of(HttpStatus.NOT_FOUND, "/errors/notFound.html");
        }
        return HttpSuccessfulResponse.of(HttpStatus.OK, request.getUrl());
    }
}
