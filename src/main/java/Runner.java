import webserver.WebServer;

public class Runner {
    public static void main(String[] args) throws Exception {
        final WebServer webServer = new WebServer();
        webServer.run(args);
    }
}
