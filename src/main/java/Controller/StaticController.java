package Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static util.HttpResponseUtils.response200Header;
import static util.HttpResponseUtils.responseBody;

public class StaticController {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static void view(String url, DataOutputStream dos, Map<String, String> requestHeader) throws IOException {
        log.debug("static url : {}",url);
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        String contentType = requestHeader.get("Accept").split(",")[0];
        response200Header(dos, contentType, body.length);
        responseBody(dos, body);
    }
}
