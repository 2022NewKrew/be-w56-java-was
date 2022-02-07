package webserver.service;

import domain.User;
import network.HttpRequest;
import network.Status;

import java.util.Map;

public class UserService {

    private HttpRequest httpRequest;
    public UserService(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
    }

    public Status signUp(){

        Map<String, String> body = httpRequest.getBody();
        if(!body.isEmpty()){
            User user = new User(body);
            return Status.FOUND;
        }
        return Status.NOTFOUND;
    }

    public Status home(){
        return Status.OK;
    }
}
