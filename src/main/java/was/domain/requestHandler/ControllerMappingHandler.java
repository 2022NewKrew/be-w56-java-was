package was.domain.requestHandler;

import di.annotation.Bean;
import was.domain.controller.ControllerMapper;
import was.domain.controller.methodInvocation.MethodInvocation;
import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;
import was.domain.requestHandler.requestHandlerChain.RequestHandlerChain;

import java.util.Optional;

@Bean
public class ControllerMappingHandler implements RequestHandler {
    private final ControllerMapper controllerMapper;

    public ControllerMappingHandler(ControllerMapper controllerMapper) {
        this.controllerMapper = controllerMapper;
    }

    @Override
    public void handle(HttpRequest req, HttpResponse res, RequestHandlerChain requestHandlerChain) {
        final Optional<MethodInvocation> optionalController = controllerMapper.getController(req);

        if (optionalController.isEmpty()) {
            res.setViewPath(req.getPath());
            requestHandlerChain.handle(req, res);
            return;
        }

        final MethodInvocation controller = optionalController.get();
        controller.invoke(req, res);

        requestHandlerChain.handle(req, res);
    }
}
