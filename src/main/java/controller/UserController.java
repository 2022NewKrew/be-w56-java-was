package controller;

import model.User;
import org.json.simple.parser.ParseException;
import service.UserService;
import util.HttpResponseUtils;
import webserver.request.HttpRequestMethod;
import webserver.request.Request;
import webserver.request.RequestUri;
import webserver.response.HttpStatusCode;
import webserver.response.Response;

import java.io.DataOutputStream;
import java.io.IOException;

public class UserController {
    private static final String REQUEST_MAPPING = "/user";
    private static final UserService userService = UserService.getInstance();

    private static UserController instance;

    private UserController() {}

    public static synchronized UserController getInstance() {
        if(instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public Response handle(Request request) throws IOException {
        HttpRequestMethod method = request.getRequestLine().getHttpRequestMethod();
        String path = request.getRequestLine().getRequestUri().getPath();
        if (method == HttpRequestMethod.GET && path.equals(REQUEST_MAPPING + "/create")){
            return create(request);
        }
        return null;
    }


    private Response create(Request request) throws IOException {
        RequestUri uri = request.getRequestLine().getRequestUri();
        User user = new User(
                uri.getParam("userId"),
                uri.getParam("password"),
                uri.getParam("name"),
                uri.getParam("email")
        );
        userService.create(user);
        return HttpResponseUtils.createResponse(request, HttpStatusCode.OK);
    }
}
