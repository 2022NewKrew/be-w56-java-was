package webserver;

import Controllers.Controller;
import Controllers.FormController;
import Controllers.UserController;
import model.Request;
import model.Response;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class RequestMapper {

    public Controller getController(Request request){
        String url = request.getRequestHeader().getRequestLine().getUrl();
        if(url.startsWith("/user") && !url.endsWith("html")){
            return new UserController();
        }
        return new FormController();
    }
}
