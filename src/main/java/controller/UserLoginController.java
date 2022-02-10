package controller;

import controller.request.Request;
import controller.response.Response;
import model.User;
import service.UserService;

/**
 * Created by melodist
 * Date: 2022-01-27 027
 * Time: 오전 11:25
 */
public class UserLoginController implements WebController{

    @Override
    public Response process(Request request) {
        User loginUser = login(request.getBody("userId"), request.getBody("password"));

        if (loginUser == null) {
            return new Response.Builder()
                    .redirect()
                    .header("Set-Cookie", "loggedIn=false; Path=/")
                    .header("Location", "/user/login_failed.html")
                    .build();
        }

        return new Response.Builder()
                .redirect()
                .header("Set-Cookie", "loggedIn=true; Path=/")
                .header("Location", "/")
                .header("Set-Cookie", "loggedInUser=" + loginUser.getUserId() + "; Path=/")
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
