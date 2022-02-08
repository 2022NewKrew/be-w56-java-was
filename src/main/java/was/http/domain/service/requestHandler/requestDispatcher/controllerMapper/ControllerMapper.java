package was.http.domain.service.requestHandler.requestDispatcher.controllerMapper;

import di.annotation.Bean;
import was.http.domain.request.HttpRequest;
import was.http.domain.request.MethodAndPath;

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
