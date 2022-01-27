package api;

import db.DataBase;
import model.User;
import webserver.dispatcher.dynamic.bind.annotation.*;
import webserver.request.HttpRequest;
import webserver.request.RequestContext;

import java.util.Map;

@RestController
public class UserController {

    /**
     * Step2 요구 사항에 따른 메소드입니다.
     */
    @GetMapping("/user/create")
    public String userCreateByGet() {
        HttpRequest request = RequestContext.getInstance().getHttpRequest();
        Map<String, String> queries = request.getUri().getQueries();
        User user = createUserBy(queries);
        DataBase.addUser(user);
        return "Welcome Bro!";
    }

    private User createUserBy(Map<String, String> queries) {
        String id = queries.get("userId");
        String password = queries.get("password");
        String name = queries.get("name");
        String email = queries.get("email");
        return new User(id, password, name, email);
    }
}
