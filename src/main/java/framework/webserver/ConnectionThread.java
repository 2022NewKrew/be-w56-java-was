package framework.webserver;

import framework.controller.FrontController;
import framework.util.Cookies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import static framework.util.Constants.SESSION_ID_KEY;

public class ConnectionThread extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionThread.class);

    private final Socket connection;
    private final FrontController frontController = FrontController.getInstance();

    public ConnectionThread(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        LOGGER.debug("Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        // try-with-resources
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
            // 요정 정보를 담을 객체
            HttpRequestHandler request = new HttpRequestHandler(br);

            // 응답 정보를 담을 객체
            HttpResponseHandler response = new HttpResponseHandler(dos);

            // Session ID 설정
            Cookies cookies = request.getCookies();

            if (!cookies.contains(SESSION_ID_KEY)) {
                response.setCookie(SESSION_ID_KEY, HttpSessionHandler.makeSession());
            }

            // Front Controller가 처리
            frontController.process(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }
}
