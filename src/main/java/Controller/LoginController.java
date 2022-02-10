package Controller;

import static service.LoginService.LOGIN_FAILED;

import java.sql.SQLException;
import java.util.Map;
import model.LoginRequest;
import service.LoginService;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public class LoginController implements Controller {

    @Override
    public void process(HttpRequest request, HttpResponse response) throws SQLException, ClassNotFoundException {
        Map<String, String> queryData = request.getQueryData();
        LoginRequest loginRequest = LoginRequest.from(queryData);

        int sessionId = LoginService.login(loginRequest);

        if (sessionId == LOGIN_FAILED) {
            response.redirectLoginFailPage();
            return;
        }

        response.setCookie(sessionId);
        response.redirectBasicPage();
    }
}
