package webserver;

import controllers.Controller;
import controllers.FormController;
import controllers.UserController;
import model.Request;

public class RequestMapper {

    public Controller getController(Request request) {
        String url = request.getRequestHeader().getRequestLine().getUrl();
        if (url.startsWith("/user") && !url.endsWith("html")) {
            return new UserController();
        }
        return new FormController();
    }
}
