package was.domain.requestHandler;

import di.annotation.Bean;
import was.domain.controller.Controller;
import was.domain.controller.StaticResourceController;
import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;
import was.domain.router.Router;

import java.io.IOException;

@Bean
public class ControllerMappingHandler implements RequestHandler {
    private final Router router;
    private final Controller staticResourceController;

    public ControllerMappingHandler(Router router, StaticResourceController staticResourceController) {
        this.router = router;
        this.staticResourceController = staticResourceController;
    }

    @Override
    public void handle(HttpRequest req, HttpResponse res) throws IOException {
        final Controller controller = router.getController(req)
                .orElse(staticResourceController);

        controller.handle(req, res);
    }
}
