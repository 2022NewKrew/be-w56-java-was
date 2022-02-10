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
import java.util.List;

public class UserListController implements MyController {

    private final MemberRepository memberRepository;

    public UserListController() {
        AppConfig appConfig = new AppConfig();
        this.memberRepository = appConfig.memberRepository();
    }

    @Override
    public ModelView process(MyHttpRequest request, MyHttpResponse response, MySession session) throws IOException, SQLException {
        if (request.getMethod() == MyHttpStatus.GET) {
            return get(request, response);
        }
        return null;
    }

    private ModelView get(MyHttpRequest request, MyHttpResponse response) throws IOException, SQLException {
        ModelView mv = new ModelView("/user/list");
        List<Member> userList = memberRepository.findAll();

        mv.getModel().put("users", userList);

        return mv;
    }
}
