package frontcontroller.controller;

import db.DataBase;
import frontcontroller.MyController;
import model.User;
import util.MyHttpRequest;
import util.MyHttpResponse;
import util.MyHttpStatus;
import util.MyRequestDispatcher;

import java.io.IOException;

public class MemberLoginController implements MyController {

    @Override
    public void process(MyHttpRequest request, MyHttpResponse response) throws IOException {
        if (request.getMethod() == MyHttpStatus.POST) {
            post(request, response);
        } else if (request.getMethod() == MyHttpStatus.GET) {
            get(request, response);
        }
    }

    private void get(MyHttpRequest request, MyHttpResponse response) throws IOException {
        MyRequestDispatcher dispatcher = request.getRequestDispatcher("/user/login.html");
        dispatcher.forward(request, response);
    }

    private void post(MyHttpRequest request, MyHttpResponse response) throws IOException {

        String userId = request.getPathVariable("userId");
        String password = request.getPathVariable("password");

        User user = DataBase.findUserById(userId);

        if (user != null && user.getPassword().equals(password)) {
            response.getCookie().set("logined", true);
        } else {
            response.getCookie().set("logined", false);
        }

        MyRequestDispatcher dispatcher = request.getRequestDispatcher("redirect:/index.html");
        dispatcher.forward(request, response);

    }
}
