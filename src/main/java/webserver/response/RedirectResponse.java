package webserver.response;

public class RedirectResponse extends Response {

    private static final String HOST = "http://localhost:8080";

    RedirectResponse(String uri) {
        super(StatusCode.FOUND);
        addHeader("Location", HOST + uri);
    }
}
