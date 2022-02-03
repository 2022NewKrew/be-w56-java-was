package webserver.framwork.core;

import org.apache.tika.Tika;
import webserver.framwork.http.HttpClientErrorException;
import webserver.framwork.http.request.HttpRequest;
import webserver.framwork.http.response.HttpResponse;
import webserver.framwork.http.response.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResourceHandler {
    private static final Tika TIKA = new Tika();
    private static final ResourceHandler instance = new ResourceHandler();
    private static final String resourcePathPrefix = "./webapp";

    public static ResourceHandler getInstance() {
        return instance;
    }

    public void forward(HttpRequest request, HttpResponse response) {
        File file = new File(resourcePathPrefix + request.getUrl());

        try {
            byte[] body = Files.readAllBytes(file.toPath());
            response.setBody(body);
            response.addHeaderValue("Content-Type", TIKA.detect(file));
        } catch (IOException e) {
            e.printStackTrace();
            throw new HttpClientErrorException(HttpStatus.NotFound, "해당 리소스를 찾을 수 없습니다.");
        }
    }
}
