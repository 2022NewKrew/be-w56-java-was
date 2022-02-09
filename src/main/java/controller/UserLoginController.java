package controller;

import controller.request.Request;
import controller.response.Response;
import model.User;
import service.UserService;

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
        String loggedIn = "false";

        if (loginUser != null) {
            redirectPath = "/";
            loggedIn = "true";
            headers.put("Set-Cookie", "loggedInUser=" + loginUser.getUserId() + "; Path=/");
        }

        headers.put("Set-Cookie", "loggedIn=" + loggedIn + "; Path=/");
        headers.put("Location", redirectPath);

        return new Response.Builder()
                .redirect()
                .headers(headers)
                .build();
    }

    private User login(String requestId, String requestPassword) {
        User user = UserService.getUserByUserId(requestId);
        if (user == null) {
            return null;
        }

        if (!user.getPassword().equals(requestPassword)) {
            return null;
        }
        return user;
    }
}
