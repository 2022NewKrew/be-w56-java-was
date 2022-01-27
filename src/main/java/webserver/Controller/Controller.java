package webserver.Controller;


import model.RequestHeader;
import util.HttpResponseUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Controller {
    private static final String BASE_PATH = "./webapp";
    private static final HttpResponseUtils httpResponseUtils = new HttpResponseUtils();

    public void fetchStaticFile(DataOutputStream dos, RequestHeader header) {
        byte[] body = new byte[0];
        try {
            String path = BASE_PATH+header.getUri();
            File file = new File(path);
            body = Files.readAllBytes(file.toPath());
            httpResponseUtils.response200Header(dos, body.length);
            HttpResponseUtils.responseBody(dos, body);
        } catch (IOException e) {
            File file = new File(BASE_PATH+"/404page.html");
            try {
                body = Files.readAllBytes(file.toPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            httpResponseUtils.response404Header(dos, body.length);
            HttpResponseUtils.responseBody(dos, body);
            e.printStackTrace();
        }
    }
}
