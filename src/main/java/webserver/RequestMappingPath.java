package webserver;

import model.Request;
import model.Response;
import service.AuthService;
import util.HttpStatus;

import java.io.DataOutputStream;
import java.io.File;
import java.net.http.HttpHeaders;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
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
            AuthService.login(request);
            Map<String, String> headers = new HashMap<>();
            headers.put("Location", "/");
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
