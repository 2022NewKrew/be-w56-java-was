package was.http.domain.service.requestHandler;

import di.annotation.Bean;
import was.http.domain.request.HttpRequest;
import was.http.domain.response.HttpResponse;
import was.http.domain.service.requestHandler.requestHandlerChain.RequestHandlerChain;
import was.http.meta.HttpStatus;

@Bean
public class ExceptionHandler implements RequestHandler {

    @Override
    public void handle(HttpRequest req, HttpResponse res, RequestHandlerChain requestHandlerChain) {
        try {
            requestHandlerChain.handle(req, res);
        } catch (RuntimeException e) {
            res.setStatus(HttpStatus.FORBIDDEN);
            res.initHeaders();
            res.setBody(new byte[]{});
        }
    }
}
