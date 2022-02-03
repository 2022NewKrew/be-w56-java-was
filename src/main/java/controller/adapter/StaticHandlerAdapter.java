package controller.adapter;

import constant.GlobalConfig;
import http.header.HttpHeaders;
import http.request.HttpRequest;
import http.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static webserver.Router.WEB_ROOT;

@Slf4j
public class StaticHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(HttpRequest request) {
        File file = new File(GlobalConfig.WEB_ROOT + request.getUrl());
        return file.isFile();
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        try {
            String url = request.getUrl();
            byte[] body = Files.readAllBytes(new File(WEB_ROOT + url).toPath());
            response.addHeader(HttpHeaders.CONTENT_TYPE, getContentType(url, request.getHeader(HttpHeaders.ACCEPT)));
            response.body(body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static String getContentType(String url, String defaultValue) {
        String extension = url.substring(url.lastIndexOf(".") + 1);
        switch (extension) {
            case "html":
                return "text/html;charset=utf-8";
            case "css":
                return "text/css";
            case "js":
                return "application/javascript";
            default:
                return defaultValue;
        }
    }
}
