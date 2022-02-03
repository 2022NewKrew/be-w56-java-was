package com.kakao.webserver;

import com.kakao.http.request.HttpRequest;
import com.kakao.http.response.HttpResponse;
import com.kakao.util.ReflectionUtils;
import com.kakao.webserver.controller.HttpController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class RequestHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final List<HttpController> controllerList;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        controllerList = ReflectionUtils.getInstancesImplementedInterface(HttpController.class);
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(in);
            handleRequest(httpRequest, out);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void handleRequest(HttpRequest httpRequest, OutputStream out) throws Exception {
        for (HttpController controller : controllerList) {
            if (controller.isValidRequest(httpRequest.getMethod(), httpRequest.getUrl().getPath())) {
                HttpResponse httpResponse = controller.handleRequest(httpRequest);
                DataOutputStream dos = new DataOutputStream(out);
                dos.writeBytes(httpResponse.toString());
                dos.write(httpResponse.getBody());
                return;
            }
        }
    }
}
