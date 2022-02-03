package was.domain.requestHandler;

import di.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;
import was.domain.requestHandler.requestHandlerChain.RequestHandlerChain;
import was.meta.HttpStatus;

import java.io.IOException;

@Bean
public class ExceptionHandler implements RequestHandler {

    private final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    public void handle(HttpRequest req, HttpResponse res, RequestHandlerChain requestHandlerChain) throws Exception {
        try {
            requestHandlerChain.handle(req, res);
        } catch (RuntimeException e) {
            res.setStatus(HttpStatus.FORBIDDEN);
            res.initHeaders();
            res.setBody(new byte[]{});
        }
    }
}
