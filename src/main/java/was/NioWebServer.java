package was;

import di.BeanContext;
import was.config.NioWebServerConfig;
import was.config.NioWebServerConfigRegistry;
import was.domain.controller.ControllerMapper;
import was.domain.controller.annotation.*;
import was.domain.controller.methodInvocation.MethodInvocation;
import was.domain.eventLoop.EventLoopGroup;
import was.domain.eventLoop.EventService;
import was.domain.eventLoop.HttpService;

import java.lang.reflect.Method;
import java.util.List;

public class NioWebServer {

    private final EventLoopGroup eventLoopGroup;

    protected NioWebServer(NioWebServerConfig webConfig) {
        final NioWebServerConfigRegistry registry = initNioWebServerConfig(webConfig);

        try {
            initControllerMapper();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        eventLoopGroup = generateEventLoopGroup(registry);
    }

    public void run() {
        eventLoopGroup.run();
    }

    private NioWebServerConfigRegistry initNioWebServerConfig(NioWebServerConfig webConfig) {
        final NioWebServerConfigRegistry registry = new NioWebServerConfigRegistry();

        webConfig.port(registry);
        webConfig.workerEventLoopSize(registry);

        return registry;
    }

    private EventLoopGroup generateEventLoopGroup(NioWebServerConfigRegistry registry) {
        final BeanContext beanContext = BeanContext.getInstance();

        final int port = registry.getPort();
        final int workEventLoopSize = registry.getWorkerEventLoopSize();

        final EventService httpService = beanContext.get(HttpService.class);

        return new EventLoopGroup(port, workEventLoopSize, httpService);
    }

    private void initControllerMapper() throws IllegalAccessException {
        final BeanContext beanContext = BeanContext.getInstance();
        final ControllerMapper controllerMapper = beanContext.get(ControllerMapper.class);

        final List<Object> objects = beanContext.getAllBeans();
        for (Object object : objects) {

            if (!object.getClass().isAnnotationPresent(Controller.class))
                continue;

            final Class<?> clazz = object.getClass();

            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(GetMapping.class)) {
                    final GetMapping getMapping = method.getDeclaredAnnotation(GetMapping.class);
                    controllerMapper.register("GET", getMapping.path(), new MethodInvocation(object, method));
                    continue;
                }

                if (method.isAnnotationPresent(PostMapping.class)) {
                    final PostMapping getMapping = method.getDeclaredAnnotation(PostMapping.class);
                    controllerMapper.register("POST", getMapping.path(), new MethodInvocation(object, method));
                    continue;
                }

                if (method.isAnnotationPresent(PutMapping.class)) {
                    final PutMapping getMapping = method.getDeclaredAnnotation(PutMapping.class);
                    controllerMapper.register("PUT", getMapping.path(), new MethodInvocation(object, method));
                    continue;
                }

                if (method.isAnnotationPresent(DeleteMapping.class)) {
                    final DeleteMapping getMapping = method.getDeclaredAnnotation(DeleteMapping.class);
                    controllerMapper.register("DELETE", getMapping.path(), new MethodInvocation(object, method));
                    continue;
                }
            }
        }
    }
}
