package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.MIME;

import java.util.Map;

public class RequestController {
    private static final Logger log = LoggerFactory.getLogger(RequestController.class);

    public static HttpResponse controlRequest(HttpRequest httpRequest) {
        HttpResponse httpResponse = new HttpResponse();
        String uri = httpRequest.getPath();
        String version = httpRequest.getVersion();

        httpResponse.setVersion(version);
        httpResponse.setUri(uri);
        httpResponse.setStatusCode(HttpStatus.OK);
        httpResponse.setContentType(MIME.parse(uri));
        if (uri.startsWith("/user/create")) {
            Map<String, String> params = httpRequest.getParameters();
            if (!params.containsKey("userId") ||
                    !params.containsKey("password") ||
                    !params.containsKey("name") ||
                    !params.containsKey("email")) {
                httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);
                return httpResponse;
            }
            User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
            log.debug("User: {}", user);
            DataBase.addUser(user);
            httpResponse.setUri("/user/list.html");
        }
        return httpResponse;
    }
}
