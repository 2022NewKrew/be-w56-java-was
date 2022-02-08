package webserver.handler;

import context.Context;
import webserver.request.Request;
import webserver.response.Response;

class PostHandler implements Handler {

    private static final PostHandler postHandler = new PostHandler();

    private PostHandler() {
    }

    public static PostHandler getInstance() {
        return postHandler;
    }

    @Override
    public Response handle(Request request) throws Exception {
        return (Response) Context.invokePostMappingMethod(request.getUri(), request);
    }
}
