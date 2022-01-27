package controller;

import controller.request.Request;
import controller.response.Response;
import db.DataBase;
import model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by melodist
 * Date: 2022-01-27 027
 * Time: 오전 11:25
 */
public class UserLoginController implements WebController{

    @Override
    public Response process(Request request) {
        Map<String, String> headers = new HashMap<>();

        User loginUser = login(request.getBody("userId"), request.getBody("password"));
        String redirectPath = "/user/login_failed.html";
        String logined = "false";

        if (loginUser != null) {
            redirectPath = "/index.html";
            logined = "true";
        }

        headers.put("Set-Cookie", "logined=" + logined + "; Path=/");
        headers.put("Location", redirectPath);

        return new Response.Builder()
                .redirect()
                .headers(headers)
                .build();
    }

    private User login(String requestId, String requestPassword) {
        User user = DataBase.findUserById(requestId);
        if (user == null) {
            return null;
        }

        if (!user.getPassword().equals(requestPassword)) {
            return null;
        }
        return user;
    }
}
