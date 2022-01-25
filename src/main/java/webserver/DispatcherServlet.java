package webserver;

import controller.KinaController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotation.RequestMethod;
import webserver.model.KinaHttpRequest;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DispatcherServlet extends Thread {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final KinaController CONTROLLER = KinaController.getInstance();
    private static final ViewRenderer RENDERER = ViewRenderer.getInstance();
    private Socket connection;

    public DispatcherServlet(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            KinaHttpRequest httpRequest = KinaHttpRequest.of(buffer);
            DataOutputStream dos = new DataOutputStream(out);
            log.info(httpRequest.toString());
            String uriString = httpRequest.uri().getPath();
            Method method = HandlerMapping.getMethod(RequestMethod.valueOf(httpRequest.method()), uriString);
            if (method == null) { // 정적 파일 요청
                RENDERER.render(dos, httpRequest.uri().getPath());
            } else { // controller 호출
                Object result = method.invoke(CONTROLLER);
                RENDERER.render(dos, (String) result);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
