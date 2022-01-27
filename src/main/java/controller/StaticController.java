package controller;

import webserver.ResponseGenerator;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.MIME;

import java.util.Arrays;

public class StaticController implements Controller{
    public HttpResponse controlRequest(HttpRequest httpRequest){
        String path = httpRequest.getPath();
        if (Arrays.stream(MIME.values()).anyMatch(mime -> mime.isExtensionMatch(path))) {
            return ResponseGenerator.generateStaticResponse(path);
        } else {
            return ResponseGenerator.generateResponse404();
        }
    }
}
