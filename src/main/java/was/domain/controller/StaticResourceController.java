package was.domain.controller;

import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;
import was.meta.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticResourceController implements Controller {

    private StaticResourceController() {
    }

    public static StaticResourceController getInstance() {
        return StaticResourceControllerWrapper.INSTANCE;
    }

    private static class StaticResourceControllerWrapper {
        private static final StaticResourceController INSTANCE = new StaticResourceController();
    }

    @Override
    public void handle(HttpRequest req, HttpResponse res) throws IOException {
        final String RESOURCE_PATH = "src/main/resources";

        final File file = new File(RESOURCE_PATH + req.getPath());
        final byte[] body = Files.readAllBytes(file.toPath());

        res.setStatus(HttpStatus.OK);
        res.setBody(body);
    }
}
