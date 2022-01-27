package webserver.Controller;


import model.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponseUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Controller {
    private static final String BASE_PATH = "./webapp";
    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    private static final HttpResponseUtils httpResponseUtils = new HttpResponseUtils();

    public void responseStaticFile(DataOutputStream dos, RequestHeader header) {
        byte[] body;
        try {
            String path = header.getUri().equals("/") ? BASE_PATH+"/index.html" : BASE_PATH + header.getUri();
            File file = new File(path);
            body = Files.readAllBytes(file.toPath());
            httpResponseUtils.response200Header(dos, body.length);
            HttpResponseUtils.responseBody(dos, body);
        } catch (IOException e) {
            File file = new File(BASE_PATH+"/404page.html");
            try {
                body = Files.readAllBytes(file.toPath());
                httpResponseUtils.response404Header(dos, body.length);
                HttpResponseUtils.responseBody(dos, body);
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
            log.error(e.getMessage());
        }
    }
}
