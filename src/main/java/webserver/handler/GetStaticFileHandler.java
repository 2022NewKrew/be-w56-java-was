package webserver.handler;

import webserver.request.Request;
import webserver.response.Response;
import webserver.response.ResponseFactory;

public class GetStaticFileHandler implements Handler {

    private static final String STATIC_FILE_ROOT_PATH = "./webapp";

    @Override
    public Response handle(Request request) throws Exception {
        return ResponseFactory.staticFile(STATIC_FILE_ROOT_PATH + request.getUri());
    }
}
