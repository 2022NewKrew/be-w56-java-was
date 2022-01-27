package webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    private static int port;

    public static void main(String[] args) {
        initConfig(args);
        startListen();
    }

    private static void initConfig(String[] args){
        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }
    }

    private static void startListen(){
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);
            connectionLoop(listenSocket);
        } catch (IOException e) {
            log.error("exception occurred : {}", e.getMessage());
        }
    }

    private static void connectionLoop(ServerSocket listenSocket) throws IOException {
        // 클라이언트가 연결될때까지 대기한다.
        Socket connection;
        while ((connection = listenSocket.accept()) != null) {
            DispatcherServlet dispatcherServlet = new DispatcherServlet(connection);
            dispatcherServlet.start();
        }
    }
}
