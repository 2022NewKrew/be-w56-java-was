package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ResponseGenerator;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.MIME;
import webserver.http.PathInfo;

import java.util.Arrays;

public class StaticController implements Controller{
    private static final Logger log = LoggerFactory.getLogger(StaticController.class);

    public HttpResponse controlRequest(HttpRequest httpRequest){
        String path = httpRequest.getPath();

        if (MIME.isSupportedExtension(path)) {
            return ResponseGenerator.generateStaticResponse(path);
        } else {
            log.debug("Page not found");
            return ResponseGenerator.generateResponse404();
        }
    }
}
