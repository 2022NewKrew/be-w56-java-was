package webserver.handler;

import context.Context;
import exception.InvalidRequestException;
import java.io.File;
import webserver.request.Request;

public final class HandlerFactory {

    private static final String STATIC_FILE_ROOT_PATH = "./webapp";

    public static Handler createHandler(Request request) throws Exception {
        switch (request.getMethod()) {
            case GET:
                return createGetHandler(request);
            case POST:
                return createPostHandler(request);
            default:
                throw new InvalidRequestException("요청을 처리할 수 없음");
        }
    }

    private static Handler createGetHandler(Request request) throws Exception {
        if (existStaticFile(request.getUri())) {
            return new GetStaticFileHandler();
        }
        throw new InvalidRequestException(request.getUri() + ": 잘못된 경로임");
    }

    private static boolean existStaticFile(String uri) {
        File file = new File(STATIC_FILE_ROOT_PATH + uri);
        return file.isFile();
    }

    private static Handler createPostHandler(Request request) throws Exception {
        if (Context.existsPostMappingUri(request.getUri())) {
            return new PostHandler();
        }
        throw new InvalidRequestException("uri가 유효하지 않음");
    }
}
