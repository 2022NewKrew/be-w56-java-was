package webserver;

import com.google.common.collect.Lists;
import mvcframework.AnnotationHandlerMapping;
import mvcframework.HandlerExecution;
import mvcframework.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RequestHandler extends Thread {
    public static final List<HandlerMapping> mappings = Lists.newArrayList();
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public static void init() {
        AnnotationHandlerMapping ahm = new AnnotationHandlerMapping("controller");
        ahm.initialize();

        mappings.add(ahm);
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            if (getSessionId(request.getHeader("Cookie")) == null) {
                response.addHeader("Set-Cookie", "JSESSIONID=" + UUID.randomUUID());
            }

            Object handler = getHandler(request);
            try {
                if (handler == null) {
                    response.forward(request.getPath());
                } else {
                    execute(handler, request, response);
                }
            } catch (Throwable e) {
                log.error("Exception : {}", e);
                throw new RuntimeException(e.getMessage());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void execute(Object handler, HttpRequest request, HttpResponse response) throws Exception {
        if (handler instanceof HandlerExecution) {
            System.out.println("handler called");
            ((HandlerExecution) handler).handle(request, response);
        }

    }

    private String getSessionId(String cookie) {
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookie);
        return cookies.get("JSESSIONID");
    }


    private Object getHandler(HttpRequest request) {
        for (HandlerMapping handlerMapping : mappings) {
            Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }
}
