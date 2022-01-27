package was.domain.router;

import di.annotation.Bean;
import was.domain.controller.Controller;
import was.domain.http.HttpRequest;
import was.domain.http.MethodAndPath;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Bean
public class Router {

    private final Map<MethodAndPath, Controller> controllers = new HashMap<>();

    public Optional<Controller> getController(HttpRequest req) {
        return Optional.ofNullable(controllers.get(req.getMethodAndPath()));
    }

    public void registerControllers(Map<MethodAndPath, Controller> controllers) {
        this.controllers.putAll(controllers);
    }
}
