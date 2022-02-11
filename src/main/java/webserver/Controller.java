package webserver;

import db.DataBase;
import model.User;
import webserver.annotation.GetMapping;
import webserver.annotation.PostMapping;
import webserver.http.message.HttpResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class Controller {

    @GetMapping("/")
    public String getIndexPage(HttpResponse response) {

        return "/index.html";
    }

    @GetMapping("/user/create")
    public String createUserGet(HttpResponse response) {
        final Map<String, String> queryString = response.getRequest().getQueryStrings();
        final String userId = queryString.get("userId");
        final String password = queryString.get("password");
        final String name = queryString.get("name");
        final String email = queryString.get("email");

        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
        return "/user/form.html";
    }

    @PostMapping("/user/create")
    public String createUserPost(HttpResponse response) {
        final Map<String, String> requestParams = response.getRequest().getRequestParams();
        final String userId = requestParams.get("userId");
        final String password = requestParams.get("password");
        final String name = requestParams.get("name");
        final String email = requestParams.get("email");

        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
        return "redirect:/index.html";
    }

    @PostMapping("/user/login")
    public String loginUser(HttpResponse response) {
        final Map<String, String> requestParams = response.getRequest().getRequestParams();
        final String userId = requestParams.get("userId");
        final String password = requestParams.get("password");

        if(isPasswordCorrect(userId, password)) {
            response.setCookieDefault();
            return "redirect:/index.html";
        }

        return "redirect:/user/login_failed.html";
    }

    private boolean isPasswordCorrect(String userId, String password) {
        User user = DataBase.findUserById(userId);
        return user.getPassword().equals(password);
    }

    @GetMapping("/user/list")
    public String userList(HttpResponse response) {
        Map<String, String> cookies = response.getRequest().getHeader().getCookie();
        String loginCookie = cookies.get("logined");

        if(loginCookie == null) {
            return "/user/login.html";
        }

        if(loginCookie.equals("true")) {
            Map<String, Object> model  = response.getModel();
            Collection<User> users = DataBase.findAll();
            model.put("users", new ArrayList<>(users));
            return "/user/list.html";
        }

        return "/user/login.html";
    }
}
