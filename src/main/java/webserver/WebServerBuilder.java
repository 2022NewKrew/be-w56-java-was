package webserver;

import webserver.http.exception.ExceptionResolver;
import webserver.processor.handler.controller.Controller;

import java.util.ArrayList;
import java.util.List;

public class WebServerBuilder {

    private List<Controller> controllers = new ArrayList<>();
    private List<ExceptionResolver> exceptionResolvers = new ArrayList<>();
    private int threadPoolSize = 10;
    private int port = 8080;

    public WebServerBuilder addController(Controller controller) {
        this.controllers.add(controller);
        return this;
    }

    public WebServerBuilder setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        return this;
    }

    public WebServerBuilder setWebServerPort(int port) {
        this.port = port;
        return this;
    }

    public WebServerBuilder addExceptionResolver(ExceptionResolver exceptionResolver) {
        this.exceptionResolvers.add(exceptionResolver);
        return this;
    }

    public WebServer build() {
        return new WebServer(controllers, exceptionResolvers, threadPoolSize, port);
    }
}
