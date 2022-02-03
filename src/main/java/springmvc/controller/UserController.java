package springmvc.controller;

import springmvc.db.DataBase;
import model.User;

import javax.xml.crypto.Data;
import java.util.Map;

public class UserController implements Controller {

    @Override
    public String doGet(Map<String, String> param, Map<String, String> sessionCookie) {
        User user = new User(param.get("userId"), param.get("password"), param.get("name"), param.get("email"));
        DataBase.addUser(user);
        return "redirect:/index.html";
    }

    @Override
    public String doPost(Map<String, String> param, Map<String, String> body, Map<String, String> sessionCookie) {
        try {
            validateDuplicateUserId(body.get("userId"));
            User user = new User(body.get("userId"), body.get("password"), body.get("name"), body.get("email"));
            DataBase.addUser(user);
            return "redirect:/index.html";
        } catch (Exception e) {
            return "redirect:/user/form_failed.html";
        }
    }

    private void validateDuplicateUserId(String userId) {
        if (DataBase.findUserById(userId).isPresent()) {
            throw new IllegalArgumentException("아이디 중복");
        }
    }
}
