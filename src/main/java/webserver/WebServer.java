package webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;
    private static final int THREAD_POOL_SIZE = 1;
    private static final int SOCKET_TIMEOUT = 3 * 60 * 1000; // 3 mins

    public void run(String[] args) throws Exception {
        // 웹서버는 기본적으로 8080번 포트를 사용한다.
        final int port = getPortFromArgs(args);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            listen(serverSocket);
        }
    }

    private int getPortFromArgs(final String[] args) {
        if (args == null || args.length == 0) {
            return DEFAULT_PORT;
        }

        try {
            return Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            return DEFAULT_PORT;
        }
    }

    private void listen(final ServerSocket serverSocket) throws SocketException {
        Objects.requireNonNull(serverSocket);
        serverSocket.setSoTimeout(SOCKET_TIMEOUT);
        
        // 서버 스레드 풀을 생성한다.
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        Socket conn = null;
        do {
            try {
                conn = serverSocket.accept();
            } catch (IOException e) {
                continue;
            }
            
            threadPool.submit(new RequestHandler(conn));
        } while (conn != null);

        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(SOCKET_TIMEOUT, TimeUnit.MILLISECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }
    }
}
