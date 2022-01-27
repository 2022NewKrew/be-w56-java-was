package webserver.handler;

import context.Context;
import webserver.request.Request;
import webserver.response.Response;

public class PostHandler implements Handler {

    @Override
    public void handle(Request request, Response response) throws Exception {
        Context.invokePostMappingMethod(request.getUri(), request, response);
    }
}
