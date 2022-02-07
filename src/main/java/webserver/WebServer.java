package webserver;

import annotation.Inject;
import di.DependencyInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import router.RouterFunction;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Properties;

public class WebServer {

    private static final ClassLoader loader = Thread.currentThread().getContextClassLoader();
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    @Inject
    private List<RouterFunction> routers;

    public WebServer() {
        Properties properties = new Properties();
        try (InputStream is = loader.getResourceAsStream("application.properties")) {
            properties.load(is);
        } catch (IOException e) {
            log.error("Failed to load application.properties", e);
        }
        properties.forEach((k, v) -> System.setProperty(k.toString(), v.toString()));
        DependencyInjector injector = new DependencyInjector(log);
        injector.inject("", this);
    }

    public static void main(String[] args) {
        int port;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }
        new WebServer().start(port);
    }

    public void start(int port) {
        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(connection, routers);
                requestHandler.start();
            }
        } catch (IOException e) {
            log.error("Web Application Server failed to start.", e);
        }
    }
}
