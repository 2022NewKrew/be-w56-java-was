package Controller;

import java.util.Map;
import model.LoginRequest;
import service.LoginService;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public class LoginController implements Controller {

    @Override
    public void process(HttpRequest request, HttpResponse response) {
        Map<String, String> queryData = request.getQueryData();
        LoginRequest loginRequest = LoginRequest.from(queryData);

        boolean loginResult = LoginService.login(loginRequest);

        response.setCookie(loginResult);
        response.redirectBasicPage();
    }
}
