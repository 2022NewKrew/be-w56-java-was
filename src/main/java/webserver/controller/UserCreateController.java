package webserver.controller;

import webserver.Request;
import webserver.Response;

public class UserCreateController implements Controller{
    @Override
    public void start(Request request, Response response) {
        System.out.println(request.getParameters().toString());
    }
}
