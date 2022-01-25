package webserver;

import controller.KinaController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.model.KinaHttpRequest;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DispatcherServlet extends Thread {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private Socket connection;

    public DispatcherServlet(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        Object controller = new KinaController();
        
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            KinaHttpRequest httpRequest = KinaHttpRequest.of(buffer);
            log.info(httpRequest.toString());
            String uriString = httpRequest.uri().getPath().toString();

            HandlerMapping.getMethod(uriString).invoke(controller);
            log.info("getMethod: " + HandlerMapping.getMethod(uriString));
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
