package frontcontroller.controller;

import config.AppConfig;
import frontcontroller.ModelView;
import frontcontroller.MyController;
import model.Member;
import repository.MemberRepository;
import util.*;

import java.io.IOException;
import java.sql.SQLException;

public class MemberFormController implements MyController {

    private final MemberRepository memberRepository;

    public MemberFormController() {
        AppConfig appConfig = new AppConfig();
        this.memberRepository = appConfig.memberRepository();
    }

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

        Member member = new Member(userId, password, name, email);

        try {
            memberRepository.save(member);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.setStatus(MyHttpResponseStatus.FOUND);
        ModelView mv = new ModelView("redirect:/index");
        return mv;
    }
}
