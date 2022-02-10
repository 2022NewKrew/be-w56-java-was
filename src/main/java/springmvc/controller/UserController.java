package springmvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springmvc.ModelAndView;
import springmvc.db.DataBase;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpStatus;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;

public class UserController extends Controller {

    Logger logger = LoggerFactory.getLogger(Controller.class);

    @Override
    public ModelAndView doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (httpRequest.getCookies().getOrDefault("logined", "false").equals("true")){
            Map<String, Object> model = new HashMap<>();
            model.put("users", DataBase.findAll());
            return new ModelAndView("/", HttpStatus.FOUND);
        }
        return new ModelAndView("/user/login.html", HttpStatus.FOUND);
    }

    @Override
    public ModelAndView doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            Map<String, String> body = httpRequest.getBody();
            validateDuplicateUserId(body.get("userId"));
            User user = new User(body.get("userId"), body.get("password"), body.get("name"), body.get("email"));
            DataBase.addUser(user);
            return new ModelAndView("/index.html", HttpStatus.FOUND);
        } catch (Exception e) {
            return new ModelAndView("/user/form_failed.html", HttpStatus.FOUND);
        }
    }

    private void validateDuplicateUserId(String userId) {
        if (DataBase.findUserById(userId).isPresent()) {
            logger.error("아이디중복");
            throw new IllegalArgumentException("아이디 중복");
        }
    }
}
