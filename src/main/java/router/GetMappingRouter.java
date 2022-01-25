package router;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import controller.UserController;
import dto.UserCreateRequestDTO;
import http.HttpRequest;
import http.HttpResponse;
import util.Constant;

public class GetMappingRouter {
    public static void routing(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        switch (httpRequest.getUrl()) {
            case "/user/create":
                createUser(httpRequest, httpResponse);
                break;
            default:
                findPage(httpRequest, httpResponse);
        }
    }

    private static void createUser(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        Map<String, String> queryParams = httpRequest.getQueryParams();

        UserController.getInstance().create(UserCreateRequestDTO.builder()
                                                                .userId(queryParams.get("userId"))
                                                                .password(queryParams.get("password"))
                                                                .name(queryParams.get("name"))
                                                                .email(queryParams.get("email"))
                                                                .build());

        byte[] body = Files.readAllBytes(new File(Constant.ROOT_PATH + "/user/list.html").toPath());

        httpResponse.response200Header(httpRequest.getContentType(), body.length);
        httpResponse.responseBody(body);
    }

    private static void findPage(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        byte[] body = Files.readAllBytes(new File(Constant.ROOT_PATH + httpRequest.getUrl()).toPath());

        httpResponse.response200Header(httpRequest.getContentType(), body.length);
        httpResponse.responseBody(body);
    }
}
