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

public class Controller {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    private final Map<String, ControllerMethod> getRouteMap;

    public Controller() {
        getRouteMap = new HashMap<>();
        getRouteMap.put("/user/create", Controller::getUserCreate);
    }

    public Response route(Map<String, String> requestMap) {
        try {
            String[] urlPathParameter = URLDecoder.decode(requestMap.get("Url"), "UTF-8").split("\\?");
            String path = urlPathParameter[0];
            Map<String, String> parameterMap = null;
            if (urlPathParameter.length > 1) {
                String parameter = urlPathParameter[1];
                parameterMap = parseQueryString(parameter);
            }
            if (requestMap.get("Method").equals("GET")) {
                return getRouteMap.getOrDefault(path, Controller::getDefault).run(requestMap, parameterMap);
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

    private static Response getUserCreate(Map<String, String> requestMap, Map<String, String> parameterMap) {
        User user = new User(parameterMap.get("userId"), parameterMap.get("password"), parameterMap.get("name"), parameterMap.get("email"));
        DataBase.addUser(user);
        log.debug(DataBase.findAll().toString());
        return redirect(requestMap, "/index.html");
    }

    private static Response getDefault(Map<String, String> requestMap, Map<String, String> parameterMap) {
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
