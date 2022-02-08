package frontcontroller.controller;

import db.DataBase;
import frontcontroller.ModelView;
import frontcontroller.MyController;
import model.User;
import util.MyHttpRequest;
import util.MyHttpResponse;
import util.MyHttpStatus;
import util.MySession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserListController implements MyController {

    @Override
    public ModelView process(MyHttpRequest request, MyHttpResponse response, MySession session) throws IOException {
        if (request.getMethod() == MyHttpStatus.GET) {
            return get(request, response);
        }
        return null;
    }

    private ModelView get(MyHttpRequest request, MyHttpResponse response) throws IOException {
        ModelView mv = new ModelView("/user/list");
        List<User> userList = new ArrayList<>(DataBase.findAll());

        mv.getModel().put("users", userList);

        return mv;
    }
}
