package Controller;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static util.HttpResponseUtils.response200Header;
import static util.HttpResponseUtils.responseBody;

public class StaticController {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static void view(String url, DataOutputStream dos, Map<String, String> requestHeader) throws IOException {
        File file = new File("./webapp" + url);
        Path path = file.toPath();
        byte[] body = Files.readAllBytes(path);
        String contentType = new Tika().detect(file);
        log.debug("static url : {}, content type : {}, file : {}", url, contentType, path.getFileName());
        response200Header(dos, contentType, body.length);
        responseBody(dos, body);
    }
}
