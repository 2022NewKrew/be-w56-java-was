package controller;

import model.Request;
import model.Response;

public class BasicController implements Controller {

    public Response routing(String urlPath, Request request) {
        return Response.of(request.getHttpMethod(),
                request.getContextType(),
                urlPath);
    }
}
