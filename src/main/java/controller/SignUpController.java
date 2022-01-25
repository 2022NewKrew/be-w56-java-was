package controller;

import http.request.HttpRequest;
import http.resource.JavaObject;
import http.resource.JavaObjectJsonParser;
import http.resource.Resource;
import http.response.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignUpController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(SignUpController.class);

    @Override
    public boolean isSupported(HttpRequest httpRequest) {
        if ("/user/create".equals(httpRequest.getPath())) {
            return true;
        }

        return false;
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        User user = new User(httpRequest.getParam("userId"), httpRequest.getParam("password"),
                httpRequest.getParam("name"), httpRequest.getParam("email"));

        Resource resource = new JavaObject(user, new JavaObjectJsonParser());
        httpResponse.send(resource);
    }
}
