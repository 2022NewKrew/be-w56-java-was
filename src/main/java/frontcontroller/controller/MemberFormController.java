package frontcontroller.controller;

import db.DataBase;
import frontcontroller.MyController;
import model.User;
import util.MyHttpRequest;
import util.MyHttpResponse;
import util.MyHttpStatus;
import util.MyRequestDispatcher;

import java.io.IOException;

public class MemberFormController implements MyController {

    @Override
    public void process(MyHttpRequest request, MyHttpResponse response) throws IOException {

        if (request.getMethod() == MyHttpStatus.GET) {
            get(request, response);
        } else if (request.getMethod() == MyHttpStatus.POST) {
            post(request, response);
        }
    }

    private void get(MyHttpRequest request, MyHttpResponse response) throws IOException {
        MyRequestDispatcher dispatcher = request.getRequestDispatcher("/user/form.html");
        dispatcher.forward(request, response);
    }


    private void post(MyHttpRequest request, MyHttpResponse response) throws IOException {
        String userId = request.getPathVariable("userId");
        String password = request.getPathVariable("password");
        String name = request.getPathVariable("name");
        String email = request.getPathVariable("email");

        User user = new User(userId, password, name, email);
        DataBase.addUser(user);

        System.out.println(user);

        MyRequestDispatcher dispatcher = request.getRequestDispatcher("/index.html");
        dispatcher.forward(request, response);
    }
}
