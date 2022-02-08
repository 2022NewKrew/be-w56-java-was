package webserver.framwork.core.view;

import webserver.framwork.core.Model;
import webserver.framwork.core.templateengine.TemplateEngine;
import webserver.framwork.http.HttpClientErrorException;
import webserver.framwork.http.response.HttpResponse;
import webserver.framwork.http.response.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class InternalResourceView implements View {
    private static final String HTML_CONTENT_TYPE = "text/html";
    private final String location;
    private final Model model;

    public InternalResourceView(String location, Model model) {
        this.location = location;
        this.model = model;
    }

    @Override
    public String getContentType() {
        return HTML_CONTENT_TYPE;
    }

    @Override
    public void render(HttpResponse response) {
        try {
            List<String> viewBody = Files.readAllLines(new File(this.location).toPath());

            response.setBody(TemplateEngine.render(viewBody, model));
            response.addHeaderValue("Content-Type", getContentType());
            response.setStatus(HttpStatus.Ok);
        } catch (IOException e) {
            e.printStackTrace();
            throw new HttpClientErrorException(HttpStatus.NotFound, "리소스를 찾을 수 없습니다.");
        }
    }
}
