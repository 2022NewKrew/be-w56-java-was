package framework.view;

import framework.http.HttpConst;
import framework.http.HttpRequest;
import framework.http.HttpResponse;
import framework.http.HttpStatus;
import framework.util.ContentType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class InternalResourceView implements View {

    private String viewName;

    public InternalResourceView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, Object> model, HttpRequest request, HttpResponse response) {
        try {
            File file = new File(HttpConst.STATIC_ROOT + viewName);
            byte[] body = Files.readAllBytes(file.toPath());

            String contentType = ContentType.getInstance().getContentType(file);
            response.setBody(body);
            response.setHeader("Content-Type", contentType);

            if (response.getStatus() == null) {
                response.setStatus(HttpStatus.OK);
            }
        } catch (IOException e) {
            response.setStatus(HttpStatus.NOT_FOUND);
            viewName = HttpConst.ERROR_PAGE;
            render(model, request, response);
        }
    }
}
