package webserver.controller;

import model.User;
import org.apache.tika.Tika;
import service.UserService;
import webserver.core.TemplateEngine;
import webserver.core.http.HttpClientErrorException;
import webserver.core.http.request.HttpMethod;
import webserver.core.http.request.HttpRequest;
import webserver.core.http.response.HttpResponse;
import webserver.core.http.response.HttpResponseBuilder;
import webserver.core.http.response.HttpStatus;

import java.util.Map;

public class WebController {
    private static WebController webController;

    private WebController() {
    }

    public static WebController getInstance() {
        if (webController == null) {
            webController = new WebController();
        }
        return webController;
    }

    public HttpResponse route(HttpRequest request) {
        if (request.getUrl().equals("/index.html") && (request.getMethod() == HttpMethod.GET)) {
            return index(request);
        }
        if (request.getUrl().equals("/") && (request.getMethod() == HttpMethod.GET)) {
            return index(request);
        }
        if (request.getUrl().equals("/user/create") && (request.getMethod() == HttpMethod.POST)) {
            return join(request);
        }

        throw new HttpClientErrorException(HttpStatus.NotFound, request.getMethod() + "," + request.getUrl() + " 페이지를 찾을 수 없습니다.");
    }

    public HttpResponse index(HttpRequest request) {
        HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder();
        return httpResponseBuilder
                .setStatus(HttpStatus.Ok)
                .setBody(TemplateEngine.render("/index.html"))
                .build();
    }

    public HttpResponse join(HttpRequest request) {
        Map<String, String> params = request.getParams();
        User user = new User(
                params.get("userId"),
                params.get("password"),
                params.get("name"),
                params.get("email")
        );
        UserService.getInstance().joinNewUser(user);

        HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder();
        return httpResponseBuilder
                .setStatus(HttpStatus.Redirect)
                .addHeaderValue("Location", "/index.html")
                .build();
    }
}
