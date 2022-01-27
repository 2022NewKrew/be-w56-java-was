package Controller;

import java.util.Map;
import service.LoginService;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public class LoginController implements Controller {

    @Override
    public void process(HttpRequest request, HttpResponse response) {
        Map<String, String> queryData = request.getQueryData();
        boolean loginResult = LoginService.login(queryData);

        response.setCookie(loginResult);
        response.redirectBasicPage();
    }
}
