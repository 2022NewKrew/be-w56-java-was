package was.http.domain.service.requestHandler.requestDispatcher;

import di.annotation.Bean;
import was.http.domain.service.requestHandler.requestDispatcher.controllerMapper.ControllerMapper;
import was.http.domain.service.requestHandler.requestDispatcher.controllerMapper.MethodInvocation;
import was.http.domain.request.HttpRequest;
import was.http.domain.service.requestHandler.requestDispatcher.methodArgumentResolver.MethodArgumentResolverChain;
import was.http.domain.response.HttpResponse;
import was.http.domain.service.requestHandler.RequestHandler;
import was.http.domain.service.requestHandler.requestDispatcher.negotiation.NegotiationHandler;
import was.http.domain.service.requestHandler.requestDispatcher.viewResolver.ViewResolver;
import was.http.domain.service.requestHandler.requestHandlerChain.RequestHandlerChain;
import was.http.domain.service.view.View;
import was.http.domain.service.requestHandler.requestDispatcher.viewResolver.ViewResolverImpl;

@Bean
public class RequestDispatcher implements RequestHandler {
    private final ControllerMapper controllerMapper;
    private final MethodArgumentResolverChain methodArgumentResolverChain;
    private final ViewResolver viewResolver;
    private final NegotiationHandler negotiationHandler;

    public RequestDispatcher(ControllerMapper controllerMapper,
                             MethodArgumentResolverChain methodArgumentResolverChain,
                             ViewResolverImpl viewResolver,
                             NegotiationHandler negotiationHandler) {

        this.controllerMapper = controllerMapper;
        this.methodArgumentResolverChain = methodArgumentResolverChain;
        this.viewResolver = viewResolver;
        this.negotiationHandler = negotiationHandler;
    }

    @Override
    public void handle(HttpRequest req, HttpResponse res, RequestHandlerChain requestHandlerChain) {
        final MethodInvocation methodInvocation = controllerMapper.getController(req)
                .orElseThrow(() -> new RuntimeException("method not allowed"));

        final Object[] params = methodArgumentResolverChain.resolve(methodInvocation, req, res);

        final Object result = methodInvocation.invoke(params);

        final View view = viewResolver.resolve(result);

        if (view.isForward()) {
            forward(req, res, requestHandlerChain, view);
            return;
        }

        view.render(req, res);
        negotiationHandler.negotiate(view, req, res);
    }

    private void forward(HttpRequest req, HttpResponse res, RequestHandlerChain requestHandlerChain, View view) {
        req.setPath(view.getPath());
        handle(req, res, requestHandlerChain);
        requestHandlerChain.handle(req, res);
    }
}
