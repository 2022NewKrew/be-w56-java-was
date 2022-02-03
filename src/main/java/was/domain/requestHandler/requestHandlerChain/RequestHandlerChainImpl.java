package was.domain.requestHandler.requestHandlerChain;

import di.annotation.Bean;
import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;
import was.domain.requestHandler.*;
import was.meta.HttpHeaders;
import was.meta.HttpStatus;
import was.meta.MediaTypes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Bean
public class RequestHandlerChainImpl implements RequestHandlerChain{

    private final List<RequestHandler> requestHandlers;
    private Iterator<RequestHandler> iterator;

    public RequestHandlerChainImpl(ExceptionHandler exceptionHandler,
                                   ControllerMappingHandler controllerMappingHandler,
                                   StaticViewResolverHandler staticViewResolverHandler,
                                   NegotiationHandler negotiationHandler) {

        requestHandlers = List.of(
                exceptionHandler,
                controllerMappingHandler,
                staticViewResolverHandler,
                negotiationHandler);
    }

    @Override
    public void handle(HttpRequest req, HttpResponse res) {
        try {
            if (iterator.hasNext()) {
                final RequestHandler requestHandler = iterator.next();

                requestHandler.handle(req, res, this);
            }
        } catch (Exception e) {
            res.initHeaders();

            final byte[] errorMsg = "<div>500 Internal Server Error</div>".getBytes(StandardCharsets.UTF_8);

            res.setStatus(HttpStatus.INTERNAL_SEVER_ERROR);
            res.addHeader(HttpHeaders.CONTENT_TYPE, MediaTypes.TEXT_HTML.getValue());
            res.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(errorMsg.length));
            res.setBody(errorMsg);
        }
    }

    private void initIterator() {
        iterator = requestHandlers.iterator();
    }

    @Override
    public RequestHandlerChainImpl clone() {
        try {
            final RequestHandlerChainImpl clone = (RequestHandlerChainImpl) super.clone();
            clone.initIterator();

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
