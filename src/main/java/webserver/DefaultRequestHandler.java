package webserver;

public class DefaultRequestHandler {
    public static String defaultHandler(Request request) {
        if (request.getType().equals("GET")) {
            return defaultGetHandler(request);
        }

        //TODO throw Http400Error;
        return "";
    }

    private static String defaultGetHandler(Request request) {
        return request.getUri();
    }
}
