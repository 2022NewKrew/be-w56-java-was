import domain.db.MemoRepository;
import webserver.WebServer;

public class Main {

    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        int port = setPort(args);

        WebServer webServer = new WebServer();

        MemoRepository.init();

        webServer.run(port);
    }

    private static int setPort(String[] args) {
        if (args == null || args.length == 0) {
            return DEFAULT_PORT;
        } else {
            return Integer.parseInt(args[0]);
        }
    }
}
