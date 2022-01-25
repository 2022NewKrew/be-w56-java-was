package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        ServletContainer servletContainer = null;
        try {
            servletContainer = new ServletContainer();
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            HttpRequest httpRequest = HttpRequestUtils.parseRequest(br);
            HttpResponse httpResponse = new HttpResponse();
            DataOutputStream dos = new DataOutputStream(out);
            servletContainer.service(httpRequest, httpResponse);
            log.debug("response header");
            log.debug(httpResponse.headerText());
            HttpResponseUtils.res(httpResponse, dos);

        } catch (IOException | InvocationTargetException | IllegalAccessException e) {
            log.error(e.getMessage());
        }
    }
}
