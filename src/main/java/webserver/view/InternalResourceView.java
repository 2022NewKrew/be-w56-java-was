package webserver.view;

import webserver.http.HttpConst;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class InternalResourceView implements View{
    private String viewName;

    public InternalResourceView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpRequest request, HttpResponse response) {
        try{
            byte[] body = Files.readAllBytes(new File(HttpConst.STATIC_ROOT + viewName).toPath());
            response.setBody(body);
            if(response.getStatus() == null){
                response.setStatus(HttpStatus.OK);
            }
        } catch(IOException e){
            response.setStatus(HttpStatus.NOT_FOUND);
            viewName = HttpConst.ERROR_PAGE;
            render(model, request, response);
        }
    }
}
