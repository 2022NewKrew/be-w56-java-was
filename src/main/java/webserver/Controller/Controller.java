package webserver.Controller;


import lombok.extern.slf4j.Slf4j;
import model.RequestHeader;
import util.HttpResponseUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class Controller {
    private static final String BASE_PATH = "./webapp";

    public void responseStaticFile(DataOutputStream dos, RequestHeader header) {
        byte[] body;
        try {
            String path = header.getUri().equals("/") ? BASE_PATH+"/index.html" : BASE_PATH + header.getUri();
            File file = new File(path);
            body = Files.readAllBytes(file.toPath());
            String s = header.getRequestInfo().getOrDefault("Accept", "0").split(",")[0];
            HttpResponseUtils.writeStatusCode(dos, 200);
            HttpResponseUtils.writeContentType(dos, s);
            HttpResponseUtils.writeBody(dos, body);
        } catch (IOException e) {
            HttpResponseUtils.response404(dos);
            log.error(e.getMessage());
        }
    }
}
