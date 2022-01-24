package router;

import http.Request;
import http.Response;

import java.io.IOException;
import java.io.InputStream;

public class StaticRouter {

    public Response get(Request request) {
        String name = request.getPath().replaceAll("^/", "");
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream(name);
        if (is == null) {
            return Response.notFound("Not Found");
        }
        try {
            byte[] content = is.readAllBytes();
            return Response.ok(new String(content));
        } catch (IOException e) {
            return Response.error(e.getMessage());
        }
    }
}
