package was.http.domain.service.requestHandler;

import was.http.domain.service.requestHandler.requestHandlerChain.RequestHandlerChain;
import was.http.domain.request.HttpRequest;
import was.http.domain.response.HttpResponse;

public interface RequestHandler {
    void handle(HttpRequest req, HttpResponse res, RequestHandlerChain requestHandlerChain) throws Exception;
}
