package webserver.mapper;

import db.DataBase;
import dto.UserLoginRequest;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import webserver.exception.*;
import webserver.http.*;
import webserver.provider.StaticResourceProvider;
import dto.UserCreateRequest;

import java.io.DataOutputStream;
import java.util.*;

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

            HttpCookie cookie = new HttpCookie("login", "true");
            cookie.setPath("/");

            return MyHttpResponse.builder(dos)
                    .status(HttpStatus.FOUND)
                    .header("Location", "/")
                    .cookie(cookie)
                    .body(body)
                    .build();
        }
    },
    USER_LIST("/user/list", HttpMethod.GET) {
        @Override
        public MyHttpResponse handle(MyHttpRequest request, DataOutputStream dos) throws Exception {
            Map<String, HttpCookie> cookies = request.cookies();
            HttpCookie loginCookie = cookies.get("login");
            if (loginCookie == null || loginCookie.getValue().equals("false")) {
                throw new UserUnauthorizedException("에러: 접근할 수 없습니다.");
            }
            Collection<User> users = DataBase.findAll();

            byte[] body = render(users, "/user/list.html").getBytes();
            return MyHttpResponse.builder(dos)
                    .status(HttpStatus.OK)
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

    private static String render(Object model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
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
