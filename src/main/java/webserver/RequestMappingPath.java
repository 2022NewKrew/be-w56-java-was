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
            return new Response.Builder(dos)
                    .body(Files.readAllBytes(new File("./webapp" + "/index.html").toPath()))
                    .status(HttpStatus.OK)
                    .contentType(request.getContentType())
                    .build();
        }
    },
    SIGN_UP("/user/create") {
        @Override
        public Response handle(Request request, DataOutputStream dos) {
            AuthService.createUser(request);
            return new Response.Builder(dos)
                    .status(HttpStatus.FOUND)
                    .headers("Location", "/")
                    .contentType(request.getContentType())
                    .build();
        }
    },
    LOG_IN("/user/login") {
        @Override
        public Response handle(Request request, DataOutputStream dos) {
            Cookie cookie = (AuthService.login(request))? new Cookie("logined", "true") : new Cookie("logined", "false");
            return new Response.Builder(dos)
                    .status(HttpStatus.FOUND)
                    .headers("Set-Cookie", cookie.toString())
                    .headers("Location", "/")
                    .contentType(request.getContentType())
                    .build();
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
