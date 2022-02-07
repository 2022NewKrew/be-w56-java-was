package frontcontroller.controller;

import db.DataBase;
import frontcontroller.ModelView;
import frontcontroller.MyController;
import model.User;
import util.MyHttpRequest;
import util.MyHttpResponse;
import util.MyHttpStatus;

import java.io.IOException;

public class MemberLoginController implements MyController {

    @Override
    public ModelView process(MyHttpRequest request, MyHttpResponse response) throws IOException {
        if (request.getMethod() == MyHttpStatus.POST) {
            return post(request, response);
        } else if (request.getMethod() == MyHttpStatus.GET) {
            return get(request, response);
        }
        return null;
    }

    private ModelView get(MyHttpRequest request, MyHttpResponse response) throws IOException {
        ModelView mv = new ModelView("/user/login");
        return mv;
    }

    private ModelView post(MyHttpRequest request, MyHttpResponse response) throws IOException {

        String userId = request.getPathVariable("userId");
        String password = request.getPathVariable("password");

        User user = DataBase.findUserById(userId);

        if (user != null && user.getPassword().equals(password)) {
            response.getCookie().set("logined", true);
        } else {
            response.getCookie().set("logined", false);
        }

        ModelView mv = new ModelView("redirect:/index");
        return mv;
    }
}
