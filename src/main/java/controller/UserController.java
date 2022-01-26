package controller;

import model.Request;
import model.Response;
import service.UserService;

public class UserController implements Controller {

    public Response routing(String urlPath, Request request) {
        if (urlPath.equals("/create")) {
            save(request);
            return Response.of(request.getHttpMethod(),
                    request.getContextType(),
                    USER+"/list.html");
        }

        return Response.of(request.getHttpMethod(),
                request.getContextType(),
                USER+urlPath);
    }

    private void save(Request request) {
        UserService service = new UserService();
        service.save(request);
    }
}
