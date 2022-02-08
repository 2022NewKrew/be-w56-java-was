package webserver.service;

import domain.User;
import domain.UserList;
import network.HttpRequest;
import network.Status;

import java.util.Map;

public class UserService {

    private HttpRequest httpRequest;

    public UserService(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public Status signUp() {

        Map<String, String> body = httpRequest.getBody();
        if (!body.isEmpty()) {
            User user = new User(body);
            UserList.append(user.getUserId(), user);
            return Status.FOUND;
        }
        return Status.NOTFOUND;
    }

    public Status logIn() {
        Map<String, String> body = httpRequest.getBody();
        String inputId = body.get("userId");
        String inputPw = body.get("password");

        if ( isValidUser(inputId, inputPw) ){
            User user = UserList.get(inputId);
            System.out.println(httpRequest);
            return Status.OK;
        }
        return Status.FOUND;
    }

    private boolean isValidUser(String inputId, String inputPw){
        User user = UserList.get(inputId);
        return user != null && user.getPassword().equals(inputPw);
    }

    public Status home() {
        return Status.OK;
    }
}
