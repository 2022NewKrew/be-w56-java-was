package Controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import static util.HttpResponseUtils.*;

public class UserController {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static void view(DataOutputStream dos, Map<String, String> requestLine, Map<String, String> requestHeader, String userUrl) throws IOException {
        log.debug("user url : {}", userUrl);
        String method = requestLine.get("method");
        if (method.equals("GET") && userUrl.matches("/create\\?userId=.*password=.*name=.*email=.*")) {
            String queryString = userUrl.split("\\?")[1];
            log.debug("queryString : {}", queryString);
            Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
            createUser(dos, parameters);
        }
    }

    private static void createUser(DataOutputStream dos, Map<String, String> parameters) throws IOException {
        try {
            String userId = parameters.get("userId");
            String password = parameters.get("password");
            String name = URLDecoder.decode(parameters.get("name"), StandardCharsets.UTF_8);
            String email = URLDecoder.decode(parameters.get("email"), StandardCharsets.UTF_8);
            DataBase.addUser(new User(userId, password, name, email));
        } catch (NullPointerException e) {
            errorView(dos);
        }
        DataBase.findAll().forEach(user -> log.debug("user : {}", user));
        response302Header(dos, "/index.html");
    }

    private static void errorView(DataOutputStream dos) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/error.html").toPath());
        response400Header(dos, body.length);
        responseBody(dos, body);
    }
}
