package app;

import app.controller.AnonymousUserException;
import app.controller.AnonymousUserExceptionResolver;
import app.controller.UserController;
import webserver.WebServer;
import webserver.WebServerBuilder;

public class Application {

    public static void main(String[] args) throws Exception {
        WebServer server = new WebServerBuilder()
                .setWebServerPort(8080)
                .setThreadPoolSize(10)
                .addController(new UserController())
                .addExceptionResolver(new AnonymousUserExceptionResolver())
                .build();
        server.start();
    }
}
