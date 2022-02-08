package was.http.domain.service.requestHandler.requestHandlerChain;

import was.http.domain.request.HttpRequest;
import was.http.domain.response.HttpResponse;

public interface RequestHandlerChain extends Cloneable  {
    void handle(HttpRequest req, HttpResponse res);
}
