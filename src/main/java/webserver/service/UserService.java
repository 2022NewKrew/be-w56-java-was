package webserver.service;

import domain.User;
import domain.UserList;
import network.HttpRequest;

import java.util.Map;

public class UserService {

    private HttpRequest httpRequest;

    public UserService(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public void signUp() {

        Map<String, String> body = httpRequest.getBody();
        if (!body.isEmpty()) {
            User user = new User(body);
            UserList.append(user.getUserId(), user);
        }
    }

    public Boolean logIn() {
        Map<String, String> body = httpRequest.getBody();
        String inputId = body.get("userId");
        String inputPw = body.get("password");

        return isValidUser(inputId, inputPw);
    }

    private boolean isValidUser(String inputId, String inputPw){
        User user = UserList.get(inputId);
        return user != null && user.getPassword().equals(inputPw);
    }

}
