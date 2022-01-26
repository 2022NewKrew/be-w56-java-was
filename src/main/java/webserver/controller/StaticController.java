package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.request.HttpRequest;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

public class StaticController implements Controller{
    private static final Logger log = LoggerFactory.getLogger(StaticController.class);
    private static final Set<String> supportExtensions = Set.of("html", "css", "js", "ico", "eot", "svg", "ttf", "woff", "woff2", "png");
    private static final String BASE_DIRECTORY = "./webapp";

    @Override
    public boolean supports(String url) {
        if(isIndexUrl(url)){
            return true;
        }

        String[] split = url.split("\\.");
        String extensionName = split[split.length-1];
        return supportExtensions.contains(extensionName);
    }

    @Override
    public void handle(HttpRequest httpRequest, DataOutputStream dos) throws IOException {
        String filePath = getFilePath(httpRequest.getUrl());
        log.info("return file {}", filePath);

        byte[] body = Files.readAllBytes(new File(filePath).toPath());
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private String getFilePath(String url){
        if(isIndexUrl(url)){
            return String.format("%s/index.html", BASE_DIRECTORY);
        }

        return String.format("%s%s", BASE_DIRECTORY, url);
    }

    private boolean isIndexUrl(String url){
        return url.equals("/") || url.equals("");
    }
}
