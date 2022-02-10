package webserver;

import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class WebServer {

    private static final int DEFAULT_PORT = 8080;
    private static final int DEFAULT_THREAD_POOL_SIZE = 100;

    public static void main(String[] args) throws Exception {

        ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);

        // 서버소켓 생성
        try (ServerSocket listenSocket = new ServerSocket(DEFAULT_PORT)) {
            log.info("Web Application Server started {} port.", DEFAULT_PORT);

            // TODO: thread -> event loop 로 변경
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(connection);
                executorService.submit(requestHandler);
            }
        }
    }
}
