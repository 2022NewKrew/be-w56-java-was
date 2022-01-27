package controller;

import db.DataBase;
import model.Response;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.*;

import static util.HttpRequestUtils.getUrlExtension;
import static util.HttpRequestUtils.parseQueryString;
import static util.IOUtils.readBodyFromFile;

public class Controller {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    private final Map<String, ControllerMethod> getRouteMap;
    private final Map<String, ControllerMethod> postRouteMap;

    public Controller() {
        getRouteMap = new HashMap<>();
        postRouteMap = new HashMap<>();
//        getRouteMap.put("/user/create", Controller::getUserCreate);
        postRouteMap.put("/user/create", Controller::postUserCreate);
    }

    public Response route(Map<String, String> requestMap) {
        try {
            Map<String, String> headerParameterMap = null;
            Map<String, String> bodyParameterMap = null;
            String[] urlPathParameter = URLDecoder.decode(requestMap.get("Url"), "UTF-8").split("\\?");
            String path = urlPathParameter[0];
            String body = URLDecoder.decode(requestMap.getOrDefault("Body", ""), "UTF-8");
            if (urlPathParameter.length > 1) {
                String parameter = urlPathParameter[1];
                headerParameterMap = parseQueryString(parameter);
            }
            if (body.length() > 0) {
                bodyParameterMap = parseQueryString(body);
            }
            if (requestMap.get("Method").equals("GET")) {
                return getRouteMap.getOrDefault(path, Controller::getDefault).run(requestMap, headerParameterMap);
            }
            if (requestMap.get("Method").equals("POST")) {
                return postRouteMap.get(path).run(requestMap, bodyParameterMap);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private static Response redirect(Map<String, String> requestMap, String url) {
        StringBuilder sb = new StringBuilder();
        byte[] body = new byte[0];
        String contentType = "text/html";
        requestMap.put("Url", url);
        sb.append("HTTP/1.1 302 \r\n");
        sb.append("Location: " + url + "\r\n");
        sb.append("Content-Type: " + contentType + ";charset=utf-8\r\n");
        sb.append("Content-Length: " + body.length + "\r\n");
        sb.append("\r\n");
        return new Response(sb.toString(), body);
    }

    private static Response postUserCreate(Map<String, String> requestMap, Map<String, String> bodyParameterMap) {
        User user = new User(
                bodyParameterMap.get("userId"),
                bodyParameterMap.get("password"),
                bodyParameterMap.get("name"),
                bodyParameterMap.get("email"));
        DataBase.addUser(user);
        log.debug(DataBase.findAll().toString());
        return redirect(requestMap, "/index.html");
    }

    private static Response getDefault(Map<String, String> requestMap, Map<String, String> headerParameterMap) {
        StringBuilder sb = new StringBuilder();
        byte[] body = readBodyFromFile(requestMap);
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
}
