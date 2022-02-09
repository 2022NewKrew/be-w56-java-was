package app;

import app.controller.AnonymousUserExceptionResolver;
import app.controller.UserController;
import niowebserver.NioWebServer;
import webserver.WebServerBuilder;

public class Application {

    public static void main(String[] args) throws Exception {
        NioWebServer server = new WebServerBuilder()
                .setWebServerPort(8080)
                .setThreadPoolSize(8)
                .addController(new UserController())
                .addExceptionResolver(new AnonymousUserExceptionResolver())
                .buildNio();
        server.start();
    }
}
