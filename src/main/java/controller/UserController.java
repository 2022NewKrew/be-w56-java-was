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

public class UserController {
    private static final Boolean SUCCESS = true;
    private static final Boolean FAIL = false;
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
        if(method == HttpRequestMethod.POST && path.equals(REQUEST_MAPPING + "/login")){
            return login(request);
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
        User user = getUser(request);
        userService.create(user);
        return HttpResponseUtils.createResponse(request, HttpStatusCode.FOUND, ContentType.TEXT_HTML, HttpResponseUtils.getRedirection("./webapp/index.html"));
    }

    private Response login(Request request) throws IOException {
        Boolean login = userService.login(getUser(request));
        if(login == SUCCESS) return HttpResponseUtils.createResponse(request, HttpStatusCode.FOUND, ContentType.TEXT_HTML, HttpResponseUtils.getRedirection("./webapp/index.html"), SUCCESS);
        return HttpResponseUtils.createResponse(request, HttpStatusCode.FOUND, ContentType.TEXT_HTML, HttpResponseUtils.getRedirection("./webapp/user/login_failed.html"), FAIL);
    }

    private User getUser(Request request) {
        RequestBody requestBody = request.getRequestBody().parseBody();
        return new User(
                requestBody.getBody("userId"),
                requestBody.getBody("password"),
                requestBody.getBody("name"),
                requestBody.getBody("email")
        );
    }
}
