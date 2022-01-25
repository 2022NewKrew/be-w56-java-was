package webserver;

import http.Request;
import http.Response;
import http.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import handler.StaticHandler;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.function.Function;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        int port;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        StaticHandler staticHandler = new StaticHandler();

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                Map<Route, Function<Request, Response>> routes = Map.of(
                        new Route("GET", ".+"), staticHandler::get
                );
                RequestHandler requestHandler = new RequestHandler(connection, routes);
                requestHandler.start();
            }
        }
    }
}
