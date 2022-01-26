package controller;

import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static util.HttpRequestUtils.getUrlExtension;

public class Controller {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    private final Map<String, ControllerMethod> getRouteMap;
    public Controller() {
        getRouteMap = new HashMap<>();
        getRouteMap.put("/user/create", Controller::getUserCreate);
    }

    public Response route(Map<String, String> requestMap) {
        if (requestMap.get("Method").equals("GET")) {
            return getRouteMap.getOrDefault(requestMap.get("Url"), Controller::getDefault).run(requestMap);
        }
        return null;
    }

    private static Response getUserCreate(Map<String, String> requestMap) {
        log.debug("call getUserCreate");
        return null;
    }

    private static Response getDefault(Map<String, String> requestMap) {
        StringBuilder sb = new StringBuilder();
        byte[] body = readBody(requestMap);
        String contentType = "";
        List<String> textTypeList = new ArrayList<>(Arrays.asList("css", "html", "js"));
        List<String> imageTypeList = new ArrayList<>(Arrays.asList("ico", "png", "jpeg", "webp"));
        String extension = getUrlExtension(requestMap.get("Url"));
        contentType = textTypeList.stream().anyMatch(s -> s.equals(extension)) ? "text/" + extension : contentType;
        contentType = imageTypeList.stream().anyMatch(s -> s.equals(extension)) ? "image/" + extension : contentType;
        sb.append("HTTP/1.1 200 OK \r\n");
        sb.append("Content-Type: " + contentType + ";charset=utf-8\r\n");
        sb.append("Content-Length: " + body.length + "\r\n");
        sb.append("\r\n");
        //body
        return new Response(sb.toString(), body);
    }

    private static byte[] readBody(Map<String, String> requestMap){
        byte[] body = null;
        try {
            body = Files.readAllBytes(new File("./webapp" + requestMap.get("Url")).toPath());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return body;
    }
}
