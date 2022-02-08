package Controller;

import java.util.Map;
import model.LoginRequest;
import service.LoginService;
import service.SessionService;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public class LoginController implements Controller {

    @Override
    public void process(HttpRequest request, HttpResponse response) {
        Map<String, String> queryData = request.getQueryData();
        LoginRequest loginRequest = LoginRequest.from(queryData);

        boolean loginResult = LoginService.login(loginRequest);

        if (!loginResult) {
            response.redirectLoginFailPage();
            return;
        }

        int sessionId = SessionService.setSession(loginRequest.getUserId());

        response.setCookie(sessionId);
        response.redirectBasicPage();
    }
}
