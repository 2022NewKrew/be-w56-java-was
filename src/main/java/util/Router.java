package util;

import controller.UserController;
import http.HttpStatusCode;
import http.MyHttpRequest;
import http.MyHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Router {
    private static final Logger log = LoggerFactory.getLogger(Router.class);
    private static final byte[] NOT_FOUNT_MESSAGE = "없는 페이지 입니다.".getBytes();
    private static final Map<String, Function<MyHttpRequest, String>> postRoutingMap;
    private static final Map<String, Function<MyHttpRequest, String>> getRoutingMap;

    static {
        Map<String, Function<MyHttpRequest, String>> tmpMap = new HashMap<>();
        UserController controller = new UserController();
        tmpMap.put("/user/create", controller::createUser);
        getRoutingMap = Collections.unmodifiableMap(tmpMap);

        tmpMap = new HashMap<>();
        postRoutingMap = Collections.unmodifiableMap(tmpMap);
    }

    public static MyHttpResponse routing(MyHttpRequest request) throws IOException {

        switch (request.getMethod()) {
            case "POST":
                return postRouting(request);
            case "GET":
                return getRouting(request);
        }


        return new MyHttpResponse.Builder()
                .setBody(NOT_FOUNT_MESSAGE)
                .setStatusCode(HttpStatusCode.STATUS_CODE_404)
                .build();

    }

    private static MyHttpResponse getRouting(MyHttpRequest request) throws IOException {
        MyHttpResponse.Builder responseBuild = new MyHttpResponse.Builder();
        Function<MyHttpRequest, String> responseMethod = getRoutingMap.get(request.getPath());
        if (responseMethod != null) {
            String body = responseMethod.apply(request);
            responseBuild.setStatusCode(HttpStatusCode.STATUS_CODE_200)
                    .setBody(body);

        } else { //라우팅 맵에 URL에 해당하는 리소스를 찾아본다.

            File file = new File("./webapp" + request.getPath());

            if (file.isFile()) {
                byte[] body = Files.readAllBytes(file.toPath());
                responseBuild.setStatusCode(HttpStatusCode.STATUS_CODE_200)
                        .setMime(request.getMime())
                        .setBody(body);
            } else {
                responseBuild.setStatusCode(HttpStatusCode.STATUS_CODE_404)
                        .setBody(NOT_FOUNT_MESSAGE);
            }
        }
        return responseBuild.build();
    }


    private static MyHttpResponse postRouting(MyHttpRequest request) {
        MyHttpResponse.Builder responseBuild = new MyHttpResponse.Builder();
        Function<MyHttpRequest, String> responseMethod = postRoutingMap.get(request.getPath());
        if (responseMethod != null) {
            String body = responseMethod.apply(request);
            responseBuild.setStatusCode(HttpStatusCode.STATUS_CODE_200)
                    .setBody(body);
        } else {
            responseBuild.setStatusCode(HttpStatusCode.STATUS_CODE_404)
                    .setBody(NOT_FOUNT_MESSAGE);
        }
        return responseBuild.build();
    }
}
