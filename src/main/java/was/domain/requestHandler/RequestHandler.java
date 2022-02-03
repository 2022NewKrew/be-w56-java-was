package was.domain.requestHandler;

import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;
import was.domain.requestHandler.requestHandlerChain.RequestHandlerChain;

import java.io.IOException;

public interface RequestHandler {
    void handle(HttpRequest req, HttpResponse res, RequestHandlerChain requestHandlerChain) throws Exception;
}
