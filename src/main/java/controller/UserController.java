package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponse;
import util.annotation.Controller;
import util.annotation.GetMapping;
import util.annotation.PostMapping;
import webserver.RequestHandler;

import java.util.Map;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);


    @GetMapping(url="/")
    public String index(){
        return "/index.html";
    }

    @GetMapping(url="/user/create")
    public String signInByGet(Map<String, String> params){
        User user = new User(
                params.get("userId"),
                params.get("password"),
                params.get("name"),
                params.get("email")
        );
        DataBase.addUser(user);
        return "/index.html";
    }

    @PostMapping(url="/user/create")
    public String signInByPost(Map<String, String> body){
        User user = new User(
                body.get("userId"),
                body.get("password"),
                body.get("name"),
                body.get("email")
        );
        DataBase.addUser(user);
        return "redirect:/index.html";
    }

    @PostMapping(url="/user/login")
    public String login(Map<String, String> body, HttpResponse httpResponse){
        String userId = body.get("userId");
        String password = body.get("password");
        User user = DataBase.findUserById(userId);
        if(user == null || !user.passwordIs(password))
            return "redirect:/user/login_failed.html";
        httpResponse.setCookie("logined", true);
        httpResponse.setCookie("Path", "/");
        return "redirect:/index.html";
    }

    //TODO cookie가 만료되지 않는 오류를 처리해야함
    @GetMapping(url="/user/logout")
    public String logout(HttpResponse httpResponse){
        httpResponse.setCookie("logined", true);
        httpResponse.setCookie("Max-Age", 0);
        return "redirect:/index.html";
    }

}
