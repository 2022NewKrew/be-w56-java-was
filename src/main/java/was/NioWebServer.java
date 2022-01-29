package was;

import di.BeanContext;
import was.config.NioWebServerConfig;
import was.config.NioWebServerConfigRegistry;
import was.domain.eventLoop.BossEventLoop;
import was.domain.eventLoop.EventService;
import was.domain.eventLoop.WorkerEventLoop;
import was.domain.router.Router;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NioWebServer {

    private final ExecutorService threadPool;
    private final BossEventLoop bossEventLoop;
    private final List<WorkerEventLoop> workerEventLoops;

    protected NioWebServer(NioWebServerConfig webConfig) {
        final NioWebServerConfigRegistry nioWebServerConfigRegistry = initNioWebServerConfig(webConfig);

        final BeanContext beanContext = BeanContext.getInstance();

        initRouter(nioWebServerConfigRegistry, beanContext);

        final int port = nioWebServerConfigRegistry.getPort();
        final int workEventLoopSize = nioWebServerConfigRegistry.getWorkerEventLoopSize();

        final EventService eventService = beanContext.get(EventService.class);

        workerEventLoops = IntStream.range(0, workEventLoopSize)
                .mapToObj(i -> new WorkerEventLoop(eventService::doService))
                .collect(Collectors.toList());

        bossEventLoop = new BossEventLoop(port, workerEventLoops);

        threadPool = Executors.newFixedThreadPool(workEventLoopSize + 1);
    }

    private void initRouter(NioWebServerConfigRegistry nioWebServerConfigRegistry, BeanContext beanContext) {
        final Router router = beanContext.get(Router.class);
        router.registerControllers(nioWebServerConfigRegistry.getControllers());
    }

    private NioWebServerConfigRegistry initNioWebServerConfig(NioWebServerConfig webConfig) {
        final NioWebServerConfigRegistry nioWebServerConfigRegistry = new NioWebServerConfigRegistry();

        webConfig.registerController(nioWebServerConfigRegistry);
        webConfig.port(nioWebServerConfigRegistry);
        webConfig.workerEventLoopSize(nioWebServerConfigRegistry);

        return nioWebServerConfigRegistry;
    }

    public void run() {
        threadPool.execute(bossEventLoop);
        workerEventLoops.forEach(threadPool::execute);
    }
}
