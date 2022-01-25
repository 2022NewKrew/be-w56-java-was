package controller;

import http.request.HttpRequest;
import http.response.HttpResponse;
import http.resource.Resource;
import http.resource.StaticFile;

import java.io.File;

public class ResoureController implements Controller {

    public static final String DOCUMENT_ROOT = "./webapp";

    @Override
    public boolean isSupported(HttpRequest httpRequest) {
        return true;
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        String contentType = getContentType(httpRequest);
        Resource file = getStaticFile(contentType, httpRequest.getPath());
        httpResponse.send(file);
    }

    private String getContentType(HttpRequest httpRequest) {
        String acceptHeader = httpRequest.getHeader("Accept").orElse("text/html");
        String contentType = acceptHeader.split(",")[0].trim();
        return contentType;
    }

    private Resource getStaticFile(String contentType, String requestUri) {
        if ("/".equals(requestUri)) {
            return new StaticFile(contentType, new File(DOCUMENT_ROOT + "/index.html"));
        }

        return new StaticFile(contentType, new File(DOCUMENT_ROOT + requestUri));
    }
}
