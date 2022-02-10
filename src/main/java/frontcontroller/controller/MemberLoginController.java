package frontcontroller.controller;

import db.DataBase;
import frontcontroller.ModelView;
import frontcontroller.MyController;
import model.Member;
import util.MyHttpRequest;
import util.MyHttpResponse;
import util.MyHttpStatus;
import util.MySession;

import java.io.IOException;

public class MemberLoginController implements MyController {

    @Override
    public ModelView process(MyHttpRequest request, MyHttpResponse response, MySession session) throws IOException {
        if (request.getMethod() == MyHttpStatus.POST) {
            return post(request, response, session);
        } else if (request.getMethod() == MyHttpStatus.GET) {
            return get(request, response);
        }
        return null;
    }

    private ModelView get(MyHttpRequest request, MyHttpResponse response) throws IOException {
        ModelView mv = new ModelView("/user/login");
        return mv;
    }

    private ModelView post(MyHttpRequest request, MyHttpResponse response, MySession session) throws IOException {
        String userId = request.getPathVariable("userId");
        String password = request.getPathVariable("password");
        Member user = DataBase.findMemberById(userId);

        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("loginUser", user);
            session.setAttribute("logined", true);
            response.getCookie().set("logined", true);
        }

        ModelView mv = new ModelView("redirect:/");
        return mv;
    }
}
