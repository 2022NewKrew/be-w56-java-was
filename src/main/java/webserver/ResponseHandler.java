package webserver;

import network.HttpRequest;
import network.Method;
import webserver.service.UserService;

public class ResponseHandler {
    private UserService userService;
    private HttpRequest httpRequest;

    public ResponseHandler(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
        this.userService = new UserService(httpRequest);
    }

    public void run(){
        String path = httpRequest.getPath();
        Method method = httpRequest.getMethod();

        switch (method){
            case GET:
                getController(path);
                break;
            case POST:
                postController(path);
                break;
        }
    }

    private void getController(String path){

    }

    private void postController(String path){
        switch (path){
            case "/user/create":
                userService.signUp();
        }
    }
}
