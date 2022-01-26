package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.MIME;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;

public class RequestController {
    private static final String URI_BASE = "./webapp";
    private static final Logger log = LoggerFactory.getLogger(RequestController.class);

    public static HttpResponse controlRequest(HttpRequest httpRequest) {
        String path = httpRequest.getPath();

        if (path.equals("/user/create")) {
            Map<String, String> params = httpRequest.getParameters();
            try {
                User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
                log.debug("User: {}", user);
                DataBase.addUser(user);
                return generateResponse302("/user/list.html");

            } catch (Exception e) {
                return generateResponse400(e);
            }
        } else if (path.equals("/user/login")) {
            Map<String, String> params = httpRequest.getParameters();
            String userId = params.get("userId");
            String password = params.get("password");
            User user = DataBase.findUserById(userId);
            if (user == null) {
                log.debug("Login failed: User not found");
                return generateLoginFailedResponse();
            }
            if (user.getPassword().equals(password)) {
                log.debug("Login success!");
                return generateLoginResponse();
            }
            log.debug("Login failed: Password mismatch");
            return generateLoginFailedResponse();
        } else if (Arrays.stream(MIME.values()).anyMatch(mime -> mime.isExtensionMatch(path))) {
            return generateStaticResponse(path);
        } else {
            return generateResponse404();
        }
    }

    private static HttpResponse generateStaticResponse(String path) {
        try {
            byte[] body = Files.readAllBytes(new File(URI_BASE + path).toPath());

            return HttpResponse.builder()
                    .status(HttpStatus.OK)
                    .contentType(MIME.parse(path))
                    .body(body)
                    .build();
        } catch (IOException e) {
            log.error(e.getMessage());
            return generateResponse500();
        }
    }

    private static HttpResponse generateLoginResponse() {
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .header("Location", "/index.html")
                .header("Set-Cookie", "logined=true; Path=/")
                .build();
    }

    private static HttpResponse generateLoginFailedResponse() {
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .header("Location", "/user/login_failed.html")
                .header("Set-Cookie", "logined=false; Path=/")
                .build();
    }

    private static HttpResponse generateResponse302(String path) {
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .header("Location", path)
                .build();
    }

    private static HttpResponse generateResponse400(Exception e) {
        byte[] body = (HttpStatus.BAD_REQUEST + ": " + e.getMessage()).getBytes();

        return HttpResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .body(body)
                .build();
    }

    private static HttpResponse generateResponse404() {
        byte[] body = HttpStatus.NOT_FOUND.toString().getBytes();

        return HttpResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .body(body)
                .build();
    }

    private static HttpResponse generateResponse500() {
        byte[] body = HttpStatus.INTERNAL_SERVER_ERROR.toString().getBytes();

        return HttpResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body)
                .build();
    }
}
