package was.http.domain.service.requestHandler.requestHandlerChain;

import di.annotation.Bean;
import was.http.domain.service.requestHandler.ExceptionHandler;
import was.http.domain.service.requestHandler.RequestHandler;
import was.http.domain.service.requestHandler.StaticResourceRequestHandler;
import was.http.domain.service.requestHandler.requestDispatcher.RequestDispatcher;
import was.http.domain.request.HttpRequest;
import was.http.domain.response.HttpResponse;
import was.http.meta.HttpHeaders;
import was.http.meta.HttpStatus;
import was.http.meta.MediaTypes;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

@Bean
public class RequestHandlerChainImpl implements RequestHandlerChain {

    private final List<RequestHandler> requestHandlers;
    private Iterator<RequestHandler> iterator;

    public RequestHandlerChainImpl(ExceptionHandler exceptionHandler,
                                   RequestDispatcher requestDispatcher,
                                   StaticResourceRequestHandler staticResourceRequestHandler) {

        requestHandlers = List.of(
                exceptionHandler,
                staticResourceRequestHandler,
                requestDispatcher
        );
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
