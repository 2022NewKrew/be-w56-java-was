package webserver.handler;

import exception.InvalidRequestException;
import java.io.File;
import webserver.request.Request;

public final class HandlerFactory {
    private static final String STATIC_FILE_ROOT_PATH = "./webapp";

    public static Handler createHandler(Request request) throws Exception {
        switch (request.getMethod()) {
            case GET:
                return createGetHandler(request);
            default:
                throw new InvalidRequestException("요청을 처리할 수 없음");
        }
    }

    private static Handler createGetHandler(Request request) throws Exception{
        if (existStaticFile(request.getUri())) {
            return new GetStaticFileHandler();
        }
        throw new InvalidRequestException("static file에 대한 get 요청만 처리할 수 있음");
    }

    private static boolean existStaticFile(String uri) {
        File file = new File(STATIC_FILE_ROOT_PATH + uri);
        return file.isFile();
    }
}
