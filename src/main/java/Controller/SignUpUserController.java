package Controller;

import java.util.Map;
import service.SignUpService;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public class SignUpUserController implements Controller {

    @Override
    public void process(HttpRequest request, HttpResponse response) {
        Map<String, String> queryData = request.getQueryData();
        SignUpService.signUp(queryData);
        response.redirectBasicPage();
    }
}
