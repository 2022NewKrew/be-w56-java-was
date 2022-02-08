package frontcontroller.controller;

import db.DataBase;
import frontcontroller.ModelView;
import frontcontroller.MyController;
import model.User;
import util.*;

import java.io.IOException;

public class MemberFormController implements MyController {

    @Override
    public ModelView process(MyHttpRequest request, MyHttpResponse response, MySession session) throws IOException {

        if (request.getMethod() == MyHttpStatus.GET) {
            return get(request, response);
        } else if (request.getMethod() == MyHttpStatus.POST) {
            return post(request, response);
        }

        return null;
    }

    private ModelView get(MyHttpRequest request, MyHttpResponse response) throws IOException {
        ModelView mv = new ModelView("/user/form");
        return mv;
    }


    private ModelView post(MyHttpRequest request, MyHttpResponse response) throws IOException {
        String userId = request.getPathVariable("userId");
        String password = request.getPathVariable("password");
        String name = request.getPathVariable("name");
        String email = request.getPathVariable("email");

        User user = new User(userId, password, name, email);
        DataBase.addUser(user);

        response.setStatus(MyHttpResponseStatus.FOUND);
        ModelView mv = new ModelView("redirect:/index");
        return mv;
    }
}
