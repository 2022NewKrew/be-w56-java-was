package controller;

import mapper.HtmlUseConst;
import mapper.MappingConst;
import mapper.ResponseSendDataModel;
import model.UserAccount;
import model.UserAccountDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.Repository;
import repository.session.SessionNoDbUseRepository;
import service.SessionService;
import service.UserService;
import webserver.request.HttpRequest;
import webserver.session.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class UserController implements Controller{
    private final Map<String, Function<HttpRequest, ResponseSendDataModel>> methodMapper;
    private final UserService userService;
    private final SessionService sessionService;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(){
        methodMapper = new HashMap<>();
        userService = new UserService();
        sessionService = SessionService.getInstance();

        methodMapper.put("GET /form", this::getForm);
        methodMapper.put("POST /form", this::postForm);
        methodMapper.put("GET /login", this::getLogin);
        methodMapper.put("POST /login", this::postLogin);
        methodMapper.put("GET /login_failed", this::getLoginFailed);
        methodMapper.put("GET /list", this::getList);
        methodMapper.put("GET /logout", this::getLogout);
    }

    private ResponseSendDataModel getForm(HttpRequest httpRequest){
        ResponseSendDataModel result = new ResponseSendDataModel("/user/form.html", httpRequest);

        return result;
    }

    private ResponseSendDataModel postForm(HttpRequest httpRequest){
        ResponseSendDataModel result = new ResponseSendDataModel("redirect:/", httpRequest);
        Map<String, String> model = httpRequest.getBody();

        String userId = model.get("userId");
        String password = model.get("password");
        String name = model.get("name");
        String email = model.get("email");

        UserAccountDTO userAccountDTO = new UserAccountDTO.Builder()
                                            .setUserId(userId)
                                            .setPassword(password)
                                            .setName(name)
                                            .setEmail(email).build();

        try {
            userService.join(userAccountDTO);
        } catch (IllegalStateException e) {
            log.info("아이디 {}로 중복으로 회원 가입을 신청했습니다", userId);
        }

        return result;
    }

    private ResponseSendDataModel getLogin(HttpRequest httpRequest){
        ResponseSendDataModel result = new ResponseSendDataModel("/user/login.html", httpRequest);

        return result;
    }

    private ResponseSendDataModel postLogin(HttpRequest httpRequest){
        ResponseSendDataModel result = new ResponseSendDataModel("redirect:/users/login_failed", httpRequest);
        Map<String, String> model = httpRequest.getBody();

        String userId = model.get("userId");
        String password = model.get("password");

        Optional<UserAccount> findUserAccount = userService.findOne(userId);

        if(findUserAccount.isEmpty()){
            log.info("[UserController > login] DB 에서 유저 계정에서 {}로 검색에 실패했습니다.", userId);
            result.setLogin(Optional.empty());

            return result;
        }

        UserAccount userAccount = findUserAccount.get();

        if(!userService.isPasswordEqual(userAccount, password)){
            log.error("[UserController > login] {} 아이디로 로그인 시 입력한 비밀번호가 DB와 일치하지 않습니다.", userAccount.getUserId());
            result.setLogin(Optional.empty());

            return result;
        }

        result = new ResponseSendDataModel("redirect:/", httpRequest);

        if(userService.findOne(userId).isPresent()) {
            Session session = new Session(userService.findOne(userId).get());

            sessionService.join(session);

            result.setLogin(sessionService.findOne(session.getSessionId()));
        }

        return result;
    }

    private ResponseSendDataModel getLoginFailed(HttpRequest httpRequest){
        ResponseSendDataModel result = new ResponseSendDataModel("/user/login_failed.html", httpRequest);

        return result;
    }

    private ResponseSendDataModel getList(HttpRequest httpRequest){
        ResponseSendDataModel result = new ResponseSendDataModel("/user/list.html", httpRequest);

        List<UserAccount> userAccountList = userService.findAll();

        result.add(HtmlUseConst.USER_LIST, userAccountList);

        return result;
    }

    private ResponseSendDataModel getLogout(HttpRequest httpRequest){
        ResponseSendDataModel result = new ResponseSendDataModel("redirect:/", httpRequest);

        Optional<Session> session = sessionService.findOne(httpRequest.getHeader().getCookie().get("id"));

        session.ifPresent(Session::invalidate);

        result.setLogin(Optional.empty());

        return result;
    }

    @Override
    public Function<HttpRequest, ResponseSendDataModel> decideMethod(String method, String url) {
        url = method + " " + url.substring(MappingConst.USER.length());

        return methodMapper.get(url);
    }
}
