package was;

import di.BeanContext;
import was.config.NioWebServerConfig;
import was.config.NioWebServerConfigRegistry;
import was.domain.event.EventLoop;
import was.domain.event.EventService;
import was.domain.router.Router;

public class NioWebServer {

    private final EventLoop eventLoop;

    protected NioWebServer(NioWebServerConfig webConfig) {
        final NioWebServerConfigRegistry nioWebServerConfigRegistry = initNioWebServerConfig(webConfig);

        final BeanContext beanContext = BeanContext.getInstance();

        initRouter(nioWebServerConfigRegistry, beanContext);

        final int port = nioWebServerConfigRegistry.getPort();
        final EventService eventService = beanContext.get(EventService.class);

        eventLoop = new EventLoop(port, eventService);
    }

    private void initRouter(NioWebServerConfigRegistry nioWebServerConfigRegistry, BeanContext beanContext) {
        final Router router = beanContext.get(Router.class);
        router.registerControllers(nioWebServerConfigRegistry.getControllers());
    }

    private NioWebServerConfigRegistry initNioWebServerConfig(NioWebServerConfig webConfig) {
        final NioWebServerConfigRegistry nioWebServerConfigRegistry = new NioWebServerConfigRegistry();

        webConfig.registerController(nioWebServerConfigRegistry);
        webConfig.port(nioWebServerConfigRegistry);
        return nioWebServerConfigRegistry;
    }

    public void run() {
        eventLoop.run();
    }
}
