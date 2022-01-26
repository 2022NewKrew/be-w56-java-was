package was;

import was.domain.event.EventLoop;

public class NioWebServer {

    private final EventLoop eventLoop;

    public NioWebServer(NioWebServerConfig nioWebServerConfig) {
        eventLoop = new EventLoop(nioWebServerConfig.getControllers());
    }

    public void run() {
        eventLoop.run();
    }
}
