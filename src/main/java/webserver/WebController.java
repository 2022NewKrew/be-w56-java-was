package webserver;

import model.User;
import service.UserService;
import webserver.http.HttpClientErrorException;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpStatus;

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

    public byte[] route(HttpRequest request) {
        if (request.getUrl().equals("/index.html") && (request.getMethod() == HttpMethod.GET)) {
            return index();
        }
        if (request.getUrl().equals("/") && (request.getMethod() == HttpMethod.GET)) {
            return index();
        }
        if (request.getUrl().equals("/user/create") && (request.getMethod() == HttpMethod.GET)) {
            return join(request);
        }

        throw new HttpClientErrorException(HttpStatus.NotFound, request.getMethod() + "," + request.getUrl() + " 페이지를 찾을 수 없습니다.");
    }

    public byte[] index() {
        return TemplateEngine.getInstance().render("/index.html");
    }

    public byte[] join(HttpRequest request) {
        Map<String, String> params = request.getParams();
        User user = new User(
                params.get("userId"),
                params.get("password"),
                params.get("name"),
                params.get("email")
        );
        UserService.getInstance().joinNewUser(user);
        return null;
    }
}
