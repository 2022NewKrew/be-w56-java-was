package framework.handler;

import java.io.*;
import java.nio.file.Files;

import framework.modelAndView.ModelAndView;
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
    public ModelAndView handle(HttpRequest req, HttpResponse res) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + req.getPath()).toPath());
        res.addHeader("Content-Type", MIME.getMediaType(req.getPath()));
        res.setBody(body);
        res.setStatusCode(200);
        return null;
    }

    @Override
    public boolean isSupport(HttpRequest httpRequest) {
        return httpRequest.getMethod().equals(this.method) && MIME.isSupportExtension(httpRequest.getPath());
    }
}
