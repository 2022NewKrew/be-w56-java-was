package webserver.mapper;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.InvalidMethodException;
import webserver.http.HttpMethod;
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
    private final HttpMethod method;

    RequestMappingInfo(String path, HttpMethod method) {
        this.path = path;
        this.method = method;
    }

    public static MyHttpResponse handleRequest(MyHttpRequest request, DataOutputStream dos, String path) {
        if (!requestMap.containsKey(path)) {
            throw new ResourceNotFoundException("에러: 존재하지 않은 리소스입니다.");
        }
        try {
            RequestMappingInfo requestMappingInfo = requestMap.get(path);
            return handleIfValidMethod(requestMappingInfo, request, dos);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new BadRequestException("에러: 부적절한 요청입니다.");
        } catch (WebServerException e) {
            throw e;
        } catch (Exception e) {
            throw new WebServerException();
        }
    }

    private static MyHttpResponse handleIfValidMethod(RequestMappingInfo requestMappingInfo, MyHttpRequest request, DataOutputStream dos) throws Exception {
        if (!requestMappingInfo.method.name().equals(request.method())) {
            throw new InvalidMethodException("에러: 부적절한 요청 메서드입니다.");
        }
        return requestMappingInfo.handle(request, dos);
    }

    public String getPath() {
        return path;
    }

    public abstract MyHttpResponse handle(MyHttpRequest request, DataOutputStream dos) throws Exception;
}
