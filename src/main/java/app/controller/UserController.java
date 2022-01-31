package app.controller;

import app.db.DataBase;
import app.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.annotation.components.Controller;
import util.annotation.mapping.GetMapping;
import util.annotation.mapping.PostMapping;
import util.http.HttpRequest;
import util.http.HttpRequestUtils;
import util.http.HttpResponse;
import util.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//Todo DI 구조를 만들어 볼 수도 있다.
@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @GetMapping(url = "/")
    public String index() {
        return "/index.html";
    }

    @GetMapping(url = "/user/create")
    public String signInByGet(Map<String, String> params) {
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
    public String signInByPost(Map<String, String> body) {
        if (body.get("userId").equals("error"))
            throw new IllegalArgumentException("NO");

        User user = new User(
                body.get("userId"),
                body.get("password"),
                body.get("name"),
                body.get("email")
        );
        DataBase.addUser(user);
        return "redirect:/index.html";
    }

    @PostMapping(url = "/user/create/v2")
    public String signInByPostV2(User user) {
        if (user.getUserId().equals("error"))
            throw new IllegalArgumentException("NO");

        DataBase.addUser(user);
        return "redirect:/index.html";
    }

    @GetMapping(url = "/user/login")
    public String loginPage(){
        return "/user/login.html";
    }

    @PostMapping(url = "/user/login")
    public String login(Map<String, String> body, HttpResponse httpResponse) {
        String userId = body.get("userId");
        String password = body.get("password");
        User user = DataBase.findUserById(userId);
        if (user == null || !user.passwordIs(password))
            return "redirect:/user/login_failed.html";
        httpResponse.setCookie("logined", true);
        httpResponse.setCookie("Path", "/");
        httpResponse.setCookie("max-age", 100);
        // ToDo
        // cookie max-age를 설정해주니까 지워졌다? Why?
        // cookie를 객체화할 필요성이 생겼다.
        return "redirect:/index.html";
    }

    @GetMapping(url = "/user/logout")
    public String logout(HttpResponse httpResponse) {
        httpResponse.setCookie("logined", false);
        httpResponse.setCookie("Path", "/");
        httpResponse.setCookie("max-age", 0);
        return "redirect:/index.html";
    }

    @GetMapping(url = "/user/list")
    public String userList(HttpRequest httpRequest, Model model){
        Map<String, String> cookie = HttpRequestUtils.parseCookies(httpRequest.getHeader("Cookie"));
        log.debug("userList.cookie : {} ", cookie);
        if(cookie.get("logined") == null)
            return "redirect:/user/login";

        List<User> users = Arrays.asList(DataBase.findAll().toArray(new User[0]));
        model.addAttribute("users", users);

        return "/user/list.html";
    }

}
