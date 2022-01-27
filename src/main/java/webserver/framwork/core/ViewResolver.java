package webserver.framwork.core;

import webserver.framwork.http.request.HttpRequest;
import webserver.framwork.http.response.HttpResponse;
import webserver.framwork.http.response.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {
    public static ViewResolver instance;
    public static final String VIEW_PREFIX = "./webapp/";
    public static final String VIEW_POSTFIX = ".html";

    private ViewResolver() {
    }

    public static ViewResolver getInstance() {
        if (instance == null) {
            instance = new ViewResolver();
        }
        return instance;
    }

    public void resolve(String viewName, Model model, HttpRequest request, HttpResponse response) {
        int redirectLocationStartIdx = viewName.indexOf(":");
        if (redirectLocationStartIdx != -1) {
            response.setStatus(HttpStatus.Redirect);
            response.addHeaderValue("Location", viewName.substring(redirectLocationStartIdx + 1) + VIEW_POSTFIX);
            return;
        }

        String viewFileLocation = VIEW_PREFIX + viewName + VIEW_POSTFIX;

        try {
            byte[] viewBody = Files.readAllBytes(new File(viewFileLocation).toPath());
            response.setBody(TemplateEngine.render(viewBody, model));
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.NotFound);
        }
    }
}
