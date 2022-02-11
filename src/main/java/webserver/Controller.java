package webserver;

import db.DataBase;
import model.User;
import webserver.annotation.GetMapping;
import webserver.annotation.PostMapping;
import webserver.http.message.HttpResponse;

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
}
