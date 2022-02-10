package Controller;

import java.sql.SQLException;
import java.util.Map;
import model.SignUpRequest;
import service.SignUpService;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public class SignUpUserController implements Controller {

    @Override
    public void process(HttpRequest request, HttpResponse response) throws SQLException, ClassNotFoundException {
        Map<String, String> queryData = request.getQueryData();
        SignUpRequest signUpRequest = SignUpRequest.from(queryData);

        SignUpService.signUp(signUpRequest);
        response.redirectBasicPage();
    }
}
