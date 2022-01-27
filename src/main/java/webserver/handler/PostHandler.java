package webserver.handler;

import context.Context;
import webserver.request.Request;
import webserver.response.Response;

public class PostHandler implements Handler {

    @Override
    public Response handle(Request request) throws Exception {
        return (Response) Context.invokePostMappingMethod(request.getUri(), request);
    }
}
