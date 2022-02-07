package user.controller;

import controller.ControllerManager;
import db.DataBase;
import http.*;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    //회원가입
    public static String createUser(Request request) {
        Map<String, String> elements = request.getElements();

        User user = new User(elements.get("userId"),
                            elements.get("password"),
                            elements.get("name"),
                            elements.get("email"));

        if(DataBase.findUserById(user.getUserId()) != null){
            return "redirect:/index.html";
        }

        DataBase.addUser(user);

        log.debug(String.format("new user registered : %s", user.getUserId()));
        return "redirect:/index.html";
    }

    //리팩토링때 UserService 생성하여 책임을 나누자, Autowired 어노테이션을 구현해보자.
    public static String loginUser(Request request){
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

            HttpSession session = HttpSessions.getSession(sessionId);
            session.setValue("user", user);
        }

        return (canLogin ? "redirect:/index.html" : "redirect:/user/login_failed.html");
    }
}
