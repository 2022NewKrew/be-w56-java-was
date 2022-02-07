package webserver.mapper;

import dto.MemoCreateRequest;
import dto.UserLoginRequest;
import model.Memo;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.*;
import webserver.http.*;
import webserver.provider.StaticResourceProvider;
import dto.UserCreateRequest;
import webserver.repository.UserRepository;

import java.io.DataOutputStream;
import java.util.*;

import static util.TemplateEngineUtils.renderDynamicTemplate;

public enum RequestMappingInfo {

    ROOT("/", HttpMethod.GET) {
        @Override
        public HttpResponse handle(HttpRequest request, DataOutputStream dos) throws Exception {
            byte[] body = StaticResourceProvider.getBytesFromPath("/index.html");

            return HttpResponse.builder(dos)
                    .status(HttpStatus.OK)
                    .body(body)
                    .build();
        }
    },
    SIGN_UP("/user/create", HttpMethod.POST) {
        @Override
        public HttpResponse handle(HttpRequest request, DataOutputStream dos) throws Exception {
            UserCreateRequest userCreateRequest = UserCreateRequest.from(request.body());
            User user = userRepository.save(userCreateRequest.toEntity());
            log.info("New user created : {}", user);

            return HttpResponse.builder(dos)
                    .status(HttpStatus.FOUND)
                    .header("Location", "/")
                    .build();
        }
    },
    LOGIN("/user/login", HttpMethod.POST) {
        @Override
        public HttpResponse handle(HttpRequest request, DataOutputStream dos) throws Exception {
            UserLoginRequest userLoginRequest = UserLoginRequest.from(request.body());
            User user = userRepository.findByUserId(userLoginRequest.getUserId())
                    .orElseThrow(() -> new UserUnauthorizedException("에러: 존재하지 않는 유저입니다."));

            if (user.isNotValidPassword(userLoginRequest.getPassword())) {
                throw new UserUnauthorizedException("에러: 로그인에 실패했습니다.");
            }
            log.info("user login: {}", user);
            byte[] body = StaticResourceProvider.getBytesFromPath("/index.html");

            HttpCookie cookie = new HttpCookie("auth", String.valueOf(user.getId()));
            cookie.setPath("/");

            return HttpResponse.builder(dos)
                    .status(HttpStatus.FOUND)
                    .header("Location", "/")
                    .cookie(cookie)
                    .body(body)
                    .build();
        }
    },
    USER_LIST("/user/list", HttpMethod.GET) {
        @Override
        public HttpResponse handle(HttpRequest request, DataOutputStream dos) throws Exception {
            Map<String, HttpCookie> cookies = request.cookies();
            HttpCookie auth = cookies.get("auth");
            if (auth == null) {
                throw new UserUnauthorizedException("에러: 접근할 수 없습니다.");
            }
            Iterable<User> users = userRepository.findAll();

            byte[] body = renderDynamicTemplate(users, "/user/list.html").getBytes();
            return HttpResponse.builder(dos)
                    .status(HttpStatus.OK)
                    .body(body)
                    .build();
        }
    };

    private static final Logger log = LoggerFactory.getLogger(RequestMappingInfo.class);

    private static final Map<String, RequestMappingInfo> requestMap;
    private static final UserRepository userRepository;

    static {
        userRepository = new UserRepository();
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

    public abstract HttpResponse handle(HttpRequest request, DataOutputStream dos) throws Exception;
}
