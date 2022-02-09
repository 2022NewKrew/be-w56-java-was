package controller;

import model.User;
import org.json.simple.parser.ParseException;
import service.UserService;
import util.HttpResponseUtils;
import webserver.request.HttpRequestMethod;
import webserver.request.Request;
import webserver.request.RequestBody;
import webserver.request.RequestUri;
import webserver.response.ContentType;
import webserver.response.HttpStatusCode;
import webserver.response.Response;

import java.io.IOException;
import java.util.Map;

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

    public Response handle(Request request) throws IOException, ParseException {
        HttpRequestMethod method = request.getRequestLine().getHttpRequestMethod();
        String path = request.getRequestLine().getRequestUri().getPath();
        if (method == HttpRequestMethod.GET && path.equals(REQUEST_MAPPING + "/create")){
            return createByGet(request);
        }
        if (method == HttpRequestMethod.POST && path.equals(REQUEST_MAPPING + "/create")) {
            return createByPost(request);
        }
        return null;
    }

    private Response createByGet(Request request) throws IOException {
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

    private Response createByPost(Request request) throws IOException {
        RequestBody requestBody = request.getRequestBody().parseBody();
        User user = new User(
            requestBody.getBody("userId"),
            requestBody.getBody("password"),
            requestBody.getBody("name"),
            requestBody.getBody("email")
        );
        userService.create(user);
        return HttpResponseUtils.createResponse(request, HttpStatusCode.FOUND, ContentType.TEXT_HTML, HttpResponseUtils.getRedirection("./webapp/index.html"));
    }
}
