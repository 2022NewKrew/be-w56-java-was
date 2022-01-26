package was.domain.requestHandler;

import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;

import java.io.IOException;

@FunctionalInterface
public interface RequestHandler {
    void handle(HttpRequest req, HttpResponse res) throws IOException;
}
