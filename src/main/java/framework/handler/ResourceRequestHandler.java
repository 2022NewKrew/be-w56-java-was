package framework.handler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MIME;
import util.HttpRequest;
import util.HttpResponse;

public class ResourceRequestHandler extends Handler {

    private static final Logger log = LoggerFactory.getLogger(ResourceRequestHandler.class);

    private String method = "GET";

    public ResourceRequestHandler() {}

    @Override
    public String handle(HttpRequest req, HttpResponse res) {
        return req.getPath();
    }

    @Override
    public boolean isSupport(HttpRequest httpRequest) {
        return httpRequest.getMethod().equals(this.method) && MIME.isSupportExtension(httpRequest.getPath());
    }
}
