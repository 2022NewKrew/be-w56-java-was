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
import webserver.repository.MemoRepository;
import webserver.repository.UserRepository;

import java.io.DataOutputStream;
import java.time.Duration;
import java.util.*;

import static util.TemplateEngineUtils.renderDynamicTemplate;

public enum RequestMappingInfo {

    ROOT("/", HttpMethod.GET) {
        @Override
        public HttpResponse handle(HttpRequest request, DataOutputStream dos) throws Exception {
            Iterable<Memo> memos = memoRepository.findAll();

            byte[] body = renderDynamicTemplate(memos, "/index.html").getBytes();

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

            Cookie authCookie = new Cookie("auth", String.valueOf(user.getId()));
            authCookie.setPath("/");
            int secondsInHour = (int) Duration.ofHours(1).toSeconds();
            authCookie.setMaxAge(secondsInHour);

            return HttpResponse.builder(dos)
                    .status(HttpStatus.FOUND)
                    .header("Location", "/")
                    .cookie(authCookie)
                    .body(body)
                    .build();
        }
    },
    USER_LIST("/user/list", HttpMethod.GET) {
        @Override
        public HttpResponse handle(HttpRequest request, DataOutputStream dos) throws Exception {
            HttpCookie cookies = request.cookies();
            if (!cookies.containsCookie("auth")) {
                throw new UserUnauthorizedException("에러: 접근할 수 없습니다.");
            }
            Iterable<User> users = userRepository.findAll();

            byte[] body = renderDynamicTemplate(users, "/user/list.html").getBytes();
            return HttpResponse.builder(dos)
                    .status(HttpStatus.OK)
                    .body(body)
                    .build();
        }
    },
    NEW_MEMO("/memo/create", HttpMethod.POST) {
        @Override
        public HttpResponse handle(HttpRequest request, DataOutputStream dos) throws Exception {
            HttpCookie cookies = request.cookies();
            String auth = cookies.orElseThrow("auth",
                    () -> new UserUnauthorizedException("에러: 접근할 수 없습니다."));

            MemoCreateRequest memoCreateRequest = MemoCreateRequest.from(request.body(), auth);
            Memo memo = memoRepository.save(memoCreateRequest.toEntity());
            log.info("New memo created : {}", memo);

            return HttpResponse.builder(dos)
                    .status(HttpStatus.FOUND)
                    .header("Location", "/")
                    .build();
        }
    };

    private static final Logger log = LoggerFactory.getLogger(RequestMappingInfo.class);

    private static final Map<String, RequestMappingInfo> requestMap;
    private static final UserRepository userRepository;
    private static final MemoRepository memoRepository;

    static {
        userRepository = new UserRepository();
        memoRepository = new MemoRepository();
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
