package was.domain.controller;

import di.annotation.Bean;
import was.domain.controller.methodInvocation.MethodInvocation;
import was.domain.http.HttpRequest;
import was.domain.http.MethodAndPath;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Bean
public class ControllerMapper {

    private final Map<MethodAndPath, MethodInvocation> controllers = new HashMap<>();

    public Optional<MethodInvocation> getController(HttpRequest req) {
        return Optional.ofNullable(controllers.get(req.getMethodAndPath()));
    }

    public void register(String method, String path, MethodInvocation controller) {
        final MethodAndPath methodAndPath = new MethodAndPath(method, path);
        controllers.put(methodAndPath, controller);
    }
}
