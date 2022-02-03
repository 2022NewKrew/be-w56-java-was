package framework;

import util.HttpRequest;
import util.HttpResponse;
import util.MIME;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {

    public void render(String viewName, HttpRequest req, HttpResponse res) throws IOException {
        // 302 리다이렉트일 경우
        if (res.getStatusCode() == 302) {
            res.addHeader("Location", viewName);
            return ;
        }

        byte[] body = Files.readAllBytes(new File("./webapp" + viewName).toPath());
        res.addHeader("Content-Type", MIME.getMediaType(viewName));
        res.setBody(body);
    }
}
