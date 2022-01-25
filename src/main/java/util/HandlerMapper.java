package util;

import network.HttpMethod;
import network.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HandlerMapper {

    private static final Logger log = LoggerFactory.getLogger(HandlerMapper.class);

    public static ResponseBody requestMapping(HttpMethod httpMethod, String path) throws IOException {
        if (httpMethod.equals(HttpMethod.GET)) {
            switch (path) {
                case "/" : return indexView();
                default : return defaultPath(path);
            }
        }
        return null;
    }

    private static ResponseBody indexView() throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
        return new ResponseBody("200", body);
    }

    private static ResponseBody defaultPath(String path) throws IOException{
        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        return new ResponseBody("200", body);
    }
}
