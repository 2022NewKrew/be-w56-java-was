package frontcontroller.controller;

import config.AppConfig;
import frontcontroller.ModelView;
import frontcontroller.MyController;
import model.Member;
import repository.MemberRepository;
import util.MyHttpRequest;
import util.MyHttpResponse;
import util.MyHttpStatus;
import util.MySession;

import java.io.IOException;
import java.sql.SQLException;

public class MemberLoginController implements MyController {

    private final MemberRepository memberRepository;

    public MemberLoginController() {
        AppConfig appConfig = new AppConfig();
        this.memberRepository = appConfig.memberRepository();
    }

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
        Member user = null;
        try {
            user = memberRepository.findByUserIdAndPassword(userId, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (user != null) {
            System.out.println(user.getEmail());
            session.setAttribute("loginUser", user);
            session.setAttribute("logined", true);
            response.getCookie().set("logined", true);
        }

        ModelView mv = new ModelView("redirect:/");
        return mv;
    }
}
