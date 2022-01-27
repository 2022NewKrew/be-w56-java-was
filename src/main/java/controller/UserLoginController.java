package controller;

import db.DataBase;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.HttpResponseFactory;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import model.User;

public class UserLoginController implements Controller{

    private static UserLoginController instance;

    public static synchronized UserLoginController getInstance() {
        if (instance == null) {
            instance = new UserLoginController();
        }
        return instance;
    }

    @Override
    public HttpResponse run(HttpRequest request, DataOutputStream dos) throws IOException {
        Map<String, String> bodyData = request.getBodyData();
        String userId = bodyData.get("userId");
        String password = bodyData.get("password");
        User user = DataBase.findUserById(userId);
        if(user.getPassword().equals(password)){
            return loginSuccess(dos);
        }
        return loginFail(dos);
    }

    private HttpResponse loginSuccess(DataOutputStream dos) throws IOException {
        Map<String, String> result = new HashMap<>();
        result.put("url", "/index.html");
        result.put("status", "302");
        return HttpResponseFactory.getHttpResponse(result, new HashMap<>(), dos);
    }

    private HttpResponse loginFail(DataOutputStream dos) throws IOException {
        Map<String, String> result = new HashMap<>();
        result.put("url", "/login_failed.html");
        result.put("status", "401");
        return HttpResponseFactory.getHttpResponse(result, new HashMap<>(), dos);
    }
}
