package framework;

import util.HttpRequest;
import util.HttpResponse;
import util.MIME;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {

    public void render(String viewName, HttpRequest req, HttpResponse res) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + viewName).toPath());
        res.addHeader("Content-Type", MIME.findMimeMapping(viewName));
        res.setStatusCode("200");
        res.setBody(body);
    }
}
