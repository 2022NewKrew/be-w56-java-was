package webserver;

import webserver.exception.NoMatchingHandler;
import webserver.handler.HandlerMapper;
import webserver.handler.WrappedHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.view.ModelAndView;

/**
 * Serve the {@link HttpRequest}
 */
public class Dispatcher {
    private final HandlerMapper handlerMapper;

    public Dispatcher(HandlerMapper handlerMapper) {
        this.handlerMapper = handlerMapper;
    }

    /**
     * 1. Find matching WrappedHandler
     * <p>
     * 2. Invoke the handle method on WrappedHandler
     * <p>
     * 3. Resolve view and render model
     * <p>
     * 4. Handle exceptions
     *
     * @param httpRequest
     * @param httpResponse
     */
    public void serve(HttpRequest httpRequest, HttpResponse httpResponse) {
        // Todo: Add support for various exception handling
        ModelAndView modelAndView;
        try {
            WrappedHandler wrappedHandler = handlerMapper.findMatchingHandler(httpRequest);
            modelAndView = wrappedHandler.handle(httpRequest, httpResponse);
        } catch (NoMatchingHandler e) {
            // Resolve view directly from request's uri, i.e. static handling
            modelAndView = new ModelAndView(httpRequest);
        }
        modelAndView.render(httpRequest, httpResponse);
        httpResponse.setHttpStatus(HttpStatus.OK);
        httpResponse.setContentTypeWithURI(httpRequest.getPath());
    }
}
