package springmvc.controller;

import springmvc.db.DataBase;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

import javax.xml.crypto.Data;
import java.util.Map;

public class UserController extends Controller {

    @Override
    public String doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> param = httpRequest.getHeaders();
        User user = new User(param.get("userId"), param.get("password"), param.get("name"), param.get("email"));
        DataBase.addUser(user);
        return "redirect:/index.html";
    }

    @Override
    public String doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            Map<String, String> body = httpRequest.getBody();
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
