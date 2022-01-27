package webserver.mapper;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.provider.StaticResourceProvider;
import dto.UserCreateRequest;
import webserver.exception.BadRequestException;
import webserver.exception.ResourceNotFoundException;
import webserver.exception.WebServerException;
import webserver.http.HttpStatus;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

public enum RequestMappingInfo {

    ROOT("/") {
        @Override
        public MyHttpResponse handle(MyHttpRequest request, DataOutputStream dos) throws Exception {
            byte[] body = StaticResourceProvider.getBytesFromPath("/index.html");

            return MyHttpResponse.builder(dos)
                    .status(HttpStatus.OK)
                    .body(body)
                    .build();
        }
    },
    SIGN_UP("/user/create") {
        @Override
        public MyHttpResponse handle(MyHttpRequest request, DataOutputStream dos) throws Exception {
            UserCreateRequest userCreateRequest = UserCreateRequest.from(request.body());
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

    private static final Map<String, RequestMappingInfo> requestMap;

    static {
        requestMap = new HashMap<>();
        for (RequestMappingInfo value : values()) {
            requestMap.put(value.getPath(), value);
        }
    }

    private final String path;

    RequestMappingInfo(String path) {
        this.path = path;
    }

    public static MyHttpResponse handleRequest(MyHttpRequest request, DataOutputStream dos, String path) {
        if (!requestMap.containsKey(path)) {
            throw new ResourceNotFoundException("에러: 존재하지 않은 리소스입니다.");
        }
        try {
            RequestMappingInfo requestMappingInfo = requestMap.get(path);
            return requestMappingInfo.handle(request, dos);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new BadRequestException("에러: 부적절한 요청입니다.");
        } catch (Exception e) {
            throw new WebServerException();
        }
    }

    public String getPath() {
        return path;
    }

    public abstract MyHttpResponse handle(MyHttpRequest request, DataOutputStream dos) throws Exception;
}
