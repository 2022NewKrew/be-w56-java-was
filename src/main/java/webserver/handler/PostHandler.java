package webserver.handler;

import context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.Request;
import webserver.response.Response;

public class PostHandler implements Handler {

    private static final Logger log = LoggerFactory.getLogger(PostHandler.class);

    @Override
    public void handle(Request request, Response response) throws Exception {
        log.debug("posthandler.handle called");
        Context.invokePostMappingMethod(request.getUri(), request, response);
    }
}
