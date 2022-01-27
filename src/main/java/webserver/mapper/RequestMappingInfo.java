package webserver.mapper;

import db.DataBase;
import dto.UserLoginRequest;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.*;
import webserver.http.HttpMethod;
import webserver.provider.StaticResourceProvider;
import dto.UserCreateRequest;
import webserver.http.HttpStatus;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

public enum RequestMappingInfo {

    ROOT("/", HttpMethod.GET) {
        @Override
        public MyHttpResponse handle(MyHttpRequest request, DataOutputStream dos) throws Exception {
            byte[] body = StaticResourceProvider.getBytesFromPath("/index.html");

            return MyHttpResponse.builder(dos)
                    .status(HttpStatus.OK)
                    .body(body)
                    .build();
        }
    },
    SIGN_UP("/user/create", HttpMethod.POST) {
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
    },
    LOGIN("/user/login", HttpMethod.POST) {
        @Override
        public MyHttpResponse handle(MyHttpRequest request, DataOutputStream dos) throws Exception {
            UserLoginRequest userLoginRequest = UserLoginRequest.from(request.body());

            User user = DataBase.findUserById(userLoginRequest.getUserId());
            if (user == null || user.isNotValidPassword(userLoginRequest.getPassword())) {
                throw new UserUnauthorizedException("에러: 로그인에 실패했습니다.");
            }
            log.info("user login: {}", user);
            byte[] body = StaticResourceProvider.getBytesFromPath("/index.html");

            return MyHttpResponse.builder(dos)
                    .status(HttpStatus.FOUND)
                    .header("Location", "/")
                    .cookie("login", "true;Path=/")
                    .body(body)
                    .build();
        }
    };

    private static final Logger log = LoggerFactory.getLogger(RequestMappingInfo.class);

    private static final Map<String, RequestMappingInfo> requestMap;

    static {
        requestMap = new HashMap<>();
        for (RequestMappingInfo value : values()) {
            requestMap.put(value.path, value);
        }
    }

    private final String path;
    private final HttpMethod method;

    RequestMappingInfo(String path, HttpMethod method) {
        this.path = path;
        this.method = method;
    }

    public static boolean isNotValidMethod(RequestMappingInfo requestMappingInfo, String method) {
        String mappingMethod = requestMappingInfo.method.name();
        return !mappingMethod.equals(method);
    }

    public static boolean hasPath(String path) {
        return requestMap.containsKey(path);
    }

    public static RequestMappingInfo from(String path) {
        return requestMap.get(path);
    }

    public abstract MyHttpResponse handle(MyHttpRequest request, DataOutputStream dos) throws Exception;
}
