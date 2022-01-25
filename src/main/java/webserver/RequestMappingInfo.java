package webserver;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import webserver.dto.UserCreateRequest;
import webserver.http.HttpStatus;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.DataOutputStream;
import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.util.Map;

public enum RequestMappingInfo {

    ROOT("/") {
        @Override
        public void handle(MyHttpRequest request, DataOutputStream dos) throws Exception {
            byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());

            MyHttpResponse response = MyHttpResponse.builder(dos)
                    .status(HttpStatus.OK)
                    .body(body)
                    .build();

            response.writeBytes();
            response.flush();
        }
    },
    SIGN_UP("/create") {
        @Override
        public void handle(MyHttpRequest request, DataOutputStream dos) throws Exception {
            URI uri = request.uri();
            Map<String, String> parameterMap = HttpRequestUtils.parseQueryString(uri.getQuery());

            UserCreateRequest userCreateRequest = UserCreateRequest.of(parameterMap);
            User user = userCreateRequest.toEntity();
            DataBase.addUser(user);

            MyHttpResponse response = MyHttpResponse.builder(dos)
                    .status(HttpStatus.FOUND)
                    .header("Location", "/")
                    .build();

            response.writeBytes();
            response.flush();
        }
    };

    private final String path;

    RequestMappingInfo(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public abstract void handle(MyHttpRequest request, DataOutputStream dos) throws Exception;
}
