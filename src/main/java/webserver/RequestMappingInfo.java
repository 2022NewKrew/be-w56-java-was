package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static webserver.WebServer.DEFAULT_RESOURCES_DIR;

public enum RequestMappingInfo {

    ROOT("/") {
        @Override
        public MyHttpResponse handle(MyHttpRequest request, DataOutputStream dos) throws Exception {
            byte[] body = Files.readAllBytes(new File(DEFAULT_RESOURCES_DIR + "/index.html").toPath());

            return MyHttpResponse.builder(dos)
                    .status(HttpStatus.OK)
                    .body(body)
                    .build();
        }
    },
    SIGN_UP("/user/create") {
        @Override
        public MyHttpResponse handle(MyHttpRequest request, DataOutputStream dos) throws Exception {
            URI uri = request.uri();
            Map<String, String> parameterMap = HttpRequestUtils.parseQueryString(uri.getQuery());

            UserCreateRequest userCreateRequest = UserCreateRequest.of(parameterMap);
            User user = userCreateRequest.toEntity();
            DataBase.addUser(user);
            log.info("New user created : {}", user);

            return MyHttpResponse.builder(dos)
                    .status(HttpStatus.FOUND)
                    .header("Location", "/")
                    .build();
        }
    };

    private static final Logger log = LoggerFactory.getLogger(RequestMappingInfo.class);

    private final String path;

    RequestMappingInfo(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public abstract MyHttpResponse handle(MyHttpRequest request, DataOutputStream dos) throws Exception;
}
