package adaptor.in.web;

import adaptor.in.web.exception.UriNotFoundException;
import infrastructure.model.HttpRequest;
import infrastructure.model.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static infrastructure.util.ResponseHandler.response200Body;
import static infrastructure.util.ResponseHandler.response200Header;

public class StaticResourceController {

    private static final StaticResourceController INSTANCE = new StaticResourceController();
    private static final Logger log = LoggerFactory.getLogger(StaticResourceController.class);

    private StaticResourceController() {
    }

    public static StaticResourceController getInstance() {
        return INSTANCE;
    }

    public void handleFileRequest(DataOutputStream dos, HttpRequest httpRequest) {
        Path path = httpRequest.getRequestPath();
        log.debug("Static Resource Request: {}", path);
        try {
            byte[] body = Files.readAllBytes(new File("./webapp" + path.getValue()).toPath());
            response200Header(dos, path.getContentType().getMimeType(), body.length);
            response200Body(dos, body);
        } catch (IOException e) {
            throw new UriNotFoundException();
        }
    }
}
