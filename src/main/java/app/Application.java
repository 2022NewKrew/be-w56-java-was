package app;

import app.controller.UserController;
import webserver.WebServer;
import webserver.WebServerBuilder;

public class Application {

    public static void main(String[] args) throws Exception {
        WebServer server = new WebServerBuilder()
                .setWebServerPort(8080)
                .setThreadPoolSize(10)
                .addController(new UserController())
                .build();
        server.start();
    }
}
