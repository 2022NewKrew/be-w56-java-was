package dynamic.html.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import http.Response;
import webserver.WebServer;

public class DefaultParser {
    protected static final String SOURCE_ROOT = "webapp";
    protected static final String LOGIN_URL = "/users/login";
    protected static final String EMPTY_STRING = "";

    protected static final Logger log = LoggerFactory.getLogger(WebServer.class);

    public byte[] getPage(Response response, String url) {
        try {
            return Files.readAllBytes(new File(SOURCE_ROOT + url).toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return EMPTY_STRING.getBytes();
        }
    }
}
