package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ResponseGenerator;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.MIME;

import java.util.Arrays;

public class StaticController implements Controller{
    private static final Logger log = LoggerFactory.getLogger(StaticController.class);

    public HttpResponse controlRequest(HttpRequest httpRequest){
        String path = httpRequest.getPath();
        if (Arrays.stream(MIME.values()).anyMatch(mime -> mime.isExtensionMatch(path))) {
            return ResponseGenerator.generateStaticResponse(path);
        } else {
            log.debug("Page not found");
            return ResponseGenerator.generateResponse404();
        }
    }
}
