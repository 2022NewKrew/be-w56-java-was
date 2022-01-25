package webserver.view;

import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {
    private static final String STATIC_ROOT = "./webapp";

    public HttpResponse findView(HttpResponse response){
        String url = response.getUrl();

        if(url.equals("/")){
            url = "/index.html";
        }

        try{
            byte[] files = Files.readAllBytes(new File(STATIC_ROOT + url).toPath());
            response.setBody(files);
            return response;
        } catch (IOException e){
            return new HttpResponse(HttpStatus.NOT_FOUND, "/error");
        }

    }
}
