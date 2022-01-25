package webserver;

public class Application {

    public static void main(String[] args) {
        int port = WebServer.defaultPortIfNull(args);
        new WebServer(port).run();
    }
}
