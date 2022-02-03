package was;

import di.BeanContext;
import was.config.NioWebServerConfig;
import was.config.NioWebServerConfigRegistry;
import was.domain.eventLoop.EventLoopGroup;
import was.domain.eventLoop.EventService;
import was.domain.http.HttpService;
import was.domain.router.ControllerMapper;

public class NioWebServer {

    private final EventLoopGroup eventLoopGroup;

    protected NioWebServer(NioWebServerConfig webConfig) {
        final NioWebServerConfigRegistry registry = initNioWebServerConfig(webConfig);

        initRouter(registry);
        eventLoopGroup = generateEventLoopGroup(registry);
    }

    public void run() {
        eventLoopGroup.run();
    }

    private NioWebServerConfigRegistry initNioWebServerConfig(NioWebServerConfig webConfig) {
        final NioWebServerConfigRegistry registry = new NioWebServerConfigRegistry();

        webConfig.registerController(registry);
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

    private void initRouter(NioWebServerConfigRegistry registry) {
        final BeanContext beanContext = BeanContext.getInstance();

        final ControllerMapper controllerMapper = beanContext.get(ControllerMapper.class);
        controllerMapper.registerControllers(registry.getControllers());
    }
}
