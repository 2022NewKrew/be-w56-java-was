package webserver.view;

import webserver.http.HttpConst;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {
    private static ViewResolver instance;

    private ViewResolver() {
    }

    public static ViewResolver getInstance() {
        if (instance == null) {
            instance = new ViewResolver();
        }

        return instance;
    }

    public void render(DataOutputStream dos, HttpResponse response) {
        String url = response.getUrl();

        if (url.equals("/")) {
            url = "/index.html";
        }
        try {
            byte[] file = Files.readAllBytes(new File(HttpConst.STATIC_ROOT + url).toPath());
            response.setBody(file);
            response.send(dos);
        } catch (IOException e) {
            render(dos, new HttpResponse(HttpStatus.NOT_FOUND, HttpConst.ERROR_PAGE));
        }
    }
}
