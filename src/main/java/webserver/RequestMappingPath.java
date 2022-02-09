package webserver;

import db.DataBase;
import model.Request;
import model.Response;
import model.User;
import service.AuthService;
import util.Cookie;
import util.HttpStatus;
import util.MIME;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static util.TemplateEngineUtils.renderDynamicTemplate;
import static webserver.WebServer.DEFAULT_PATH;

public enum RequestMappingPath {
    ROOT("/") {
        @Override
        public Response handle(Request request, DataOutputStream dos) throws Exception {
            return new Response.Builder(dos)
                    .body(Files.readAllBytes(new File(DEFAULT_PATH + "/index.html").toPath()))
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
        public Response handle(Request request, DataOutputStream dos) throws IOException {
            if(request.method().equals("GET")){
                return new Response.Builder(dos)
                        .body(Files.readAllBytes(new File(DEFAULT_PATH + "/user/login.html").toPath()))
                        .status(HttpStatus.OK)
                        .contentType("text/html")
                        .build();
            }
            Cookie cookie = (AuthService.login(request))? new Cookie("logined", "true") : new Cookie("logined", "false");
            System.out.println(cookie);
            return new Response.Builder(dos)
                    .status(HttpStatus.FOUND)
                    .headers("Set-Cookie", cookie.toString())
                    .headers("Location", "/")
                    .contentType(request.getContentType())
                    .build();
        }
    },
    USER_LIST("/user/list") {
        @Override
        public Response handle(Request request, DataOutputStream dos) throws IOException {
            Response.Builder result = new Response.Builder(dos)
                    .contentType(request.getContentType());
            if(request.getCookies().get("logined").equals("true")){
                Collection<User> users = DataBase.findAll();
                result.status(HttpStatus.OK)
                        .body(renderDynamicTemplate(users, "/user/list.html").getBytes());
            } else {
                result.status(HttpStatus.FOUND)
                        .headers("Location", "/user/login");
            }
            return result.build();
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
