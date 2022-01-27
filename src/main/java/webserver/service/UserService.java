package webserver.service;

import domain.User;
import network.HttpRequest;

public class UserService {

    private HttpRequest httpRequest;
    public UserService(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
    }

    public void signUp(){
        User user = new User(httpRequest.getBody());
        System.out.println(user);
    }
}
