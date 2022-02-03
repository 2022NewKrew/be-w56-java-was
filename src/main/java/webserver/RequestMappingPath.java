package webserver;

import model.Request;
import model.Response;
import service.AuthService;
import util.Cookie;
import util.HttpStatus;

import java.io.DataOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public enum RequestMappingPath {
    ROOT("/") {
        @Override
        public Response handle(Request request, DataOutputStream dos) throws Exception {
            byte[] body = Files.readAllBytes(new File("./webapp" + "/index.html").toPath());
            return new Response(dos, body, HttpStatus.OK, new HashMap<>(), request.headers().map().get("Accept").get(0));
        }
    },
    SIGN_UP("/user/create") {
        @Override
        public Response handle(Request request, DataOutputStream dos) throws Exception {
            AuthService.createUser(request);
            Map<String, String> headers = new HashMap<>();
            headers.put("Location", "/");
            return new Response(dos, new byte[]{}, HttpStatus.FOUND, headers, request.headers().map().get("Accept").get(0));
        }
    },
    LOG_IN("/user/login") {
        @Override
        public Response handle(Request request, DataOutputStream dos) throws Exception {
            Map<String, String> headers = new HashMap<>();
            if (AuthService.login(request)){
                Cookie cookie = new Cookie("logined","true");
                headers.put("Set-Cookie", cookie.toString());
                headers.put("Location", "/");
            }
           else {
                Cookie cookie = new Cookie("logined","false");
                headers.put("Location", "/");
            }
            return new Response(dos, new byte[]{}, HttpStatus.FOUND, headers, request.headers().map().get("Accept").get(0));
        }
    };

    private final String path;

    RequestMappingPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public abstract Response handle(Request request, DataOutputStream dos) throws Exception;
}
