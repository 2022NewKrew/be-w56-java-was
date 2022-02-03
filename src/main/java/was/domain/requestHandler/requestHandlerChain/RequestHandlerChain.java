package was.domain.requestHandler.requestHandlerChain;

import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;

public interface RequestHandlerChain extends Cloneable  {
    void handle(HttpRequest req, HttpResponse res);
}
