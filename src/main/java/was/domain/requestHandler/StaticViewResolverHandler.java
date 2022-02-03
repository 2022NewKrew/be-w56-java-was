package was.domain.requestHandler;

import di.annotation.Bean;
import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;
import was.domain.requestHandler.requestHandlerChain.RequestHandlerChain;
import was.meta.HttpStatus;
import was.util.StaticResourceReader;

import java.io.IOException;

@Bean
public class StaticViewResolverHandler implements RequestHandler {

    private final StaticResourceReader staticResourceReader;

    public StaticViewResolverHandler(StaticResourceReader staticResourceReader) {
        this.staticResourceReader = staticResourceReader;
    }

    @Override
    public void handle(HttpRequest req, HttpResponse res, RequestHandlerChain requestHandlerChain) throws IOException {
        if (res.hasNotViewPath()) {
            requestHandlerChain.handle(req, res);
            return;
        }

        res.setStatus(HttpStatus.OK);
        res.setBody(staticResourceReader.read(res.getViewPath()));

        requestHandlerChain.handle(req, res);
    }
}
