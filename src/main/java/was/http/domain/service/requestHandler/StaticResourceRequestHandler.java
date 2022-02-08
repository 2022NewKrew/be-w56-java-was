package was.http.domain.service.requestHandler;

import di.annotation.Bean;
import was.http.domain.request.HttpRequest;
import was.http.domain.response.HttpResponse;
import was.http.domain.service.requestHandler.requestDispatcher.negotiation.NegotiationHandler;
import was.http.domain.service.requestHandler.requestDispatcher.viewResolver.StaticResourceViewResolver;
import was.http.domain.service.requestHandler.requestHandlerChain.RequestHandlerChain;
import was.http.domain.service.view.ModelAndView;
import was.http.domain.service.view.View;
import was.http.domain.service.view.ViewType;

@Bean
public class StaticResourceRequestHandler implements RequestHandler {

    private final NegotiationHandler negotiationHandler;
    private final StaticResourceViewResolver staticResourceViewResolver;

    public StaticResourceRequestHandler(NegotiationHandler negotiationHandler,
                                        StaticResourceViewResolver staticResourceViewResolver) {
        this.negotiationHandler = negotiationHandler;
        this.staticResourceViewResolver = staticResourceViewResolver;
    }

    @Override
    public void handle(HttpRequest req, HttpResponse res, RequestHandlerChain requestHandlerChain) {
        if (req.isNotStaticResource()) {
            requestHandlerChain.handle(req, res);
            return;
        }

        final ModelAndView modelAndView = new ModelAndView(ViewType.STATIC_RESOURCE, req.getPath());
        final View view = staticResourceViewResolver.resolve(modelAndView);
        view.render(req, res);
        negotiationHandler.negotiate(view, req, res);
    }
}
