package com.my.was.controller;

import com.my.was.http.request.HttpRequest;
import com.my.was.http.resource.Resource;
import com.my.was.http.resource.StaticFile;
import com.my.was.http.response.HttpResponse;

import java.io.File;

public class ResoureController {

    private static final String DOCUMENT_ROOT = "./webapp";

    public void handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        String contentType = getContentType(httpRequest);
        Resource file = getStaticFile(contentType, httpRequest.getPath());
        httpResponse.body(file);
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
