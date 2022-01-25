package adaptor.in.web;

import adaptor.in.web.exception.UriNotFoundException;
import infrastructure.model.ContentType;
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

public class HomeController {

    private static final HomeController INSTANCE = new HomeController();
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    private HomeController() {
    }

    public static HomeController getInstance() {
        return INSTANCE;
    }

    public void handle(DataOutputStream dos, HttpRequest httpRequest) {
        Path path = httpRequest.getRequestPath();
        if (path.matchHandler("")) {
            home(dos);
        }
    }

    public void home(DataOutputStream dos) {
        log.debug("Home Page Request!");
        try {
            byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
            response200Header(dos, ContentType.HTML.getMimeType(), body.length);
            response200Body(dos, body);
        } catch (IOException e) {
            throw new UriNotFoundException();
        }
    }
}
