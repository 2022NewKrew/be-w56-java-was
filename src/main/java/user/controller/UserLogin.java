package user.controller;

import controller.AbstractController;
import db.DataBase;
import http.CookieManager;
import http.Request;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UserLogin extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(UserLogin.class);

    //리팩토링때 UserService 생성하여 책임을 나누자, Autowired 어노테이션을 구현해보자.
    @Override
    public String execute(Request request) {
        Map<String, String> elements = request.getElements();

        String userId = elements.get("userId");
        String password = elements.get("password");
        boolean canLogin = true;

        User user = DataBase.findUserById(userId);

        if(user == null){
            canLogin = false;
        }

        if(!user.isSamePassword(password)){
            canLogin = false;
        }

        String sessionId = request.getCookieValue("sessionId");
        CookieManager.addNewCookie(sessionId, "logined", (canLogin ? "true" : "false"));

        if(canLogin) {
            log.debug(String.format("user logined : %s", user.getUserId()));
            request.setSessionValue("user", user);
        }

        return (canLogin ? "redirect:/index.html" : "redirect:/user/login_failed.html");
    }
}
