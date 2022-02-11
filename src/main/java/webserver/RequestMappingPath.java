package webserver;

import model.Memo;
import model.Request;
import model.Response;
import model.User;
import repository.UserRepository;
import service.AuthService;
import service.MemoService;
import util.Cookie;
import util.HttpStatus;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

import static util.TemplateEngineUtils.renderDynamicTemplate;
import static webserver.WebServer.DEFAULT_PATH;

public enum RequestMappingPath {
    ROOT("/") {
        @Override
        public Response handle(Request request, DataOutputStream dos) throws Exception {
            Collection<Memo> memos = MemoService.showMemos(request);
            return new Response.Builder(dos)
                    .body(renderDynamicTemplate(memos, "/index.html").getBytes())
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
        public Response handle(Request request, DataOutputStream dos) {
            Response.Builder result = new Response.Builder(dos)
                    .contentType(request.getContentType());
            if(request.getCookies().get("logined").equals("true")){
                Collection<User> users = userRepository.findAll();
                result.status(HttpStatus.OK)
                        .body(renderDynamicTemplate(users, "/user/list.html").getBytes());
            } else {
                result.status(HttpStatus.FOUND)
                        .headers("Location", "/user/login");
            }
            return result.build();
        }
    },
    MEMO_CREATE("/memo/create") {
        @Override
        public Response handle(Request request, DataOutputStream dos) throws Exception {
            MemoService.createMemo(request);
            return new Response.Builder(dos)
                    .status(HttpStatus.FOUND)
                    .headers("Location", "/")
                    .contentType(request.getContentType())
                    .build();
        }
    }
    ;

    private static final UserRepository userRepository;

    static {
        userRepository = new UserRepository();
    }

    private final String path;

    RequestMappingPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public abstract Response handle(Request request, DataOutputStream dos) throws Exception;
}
