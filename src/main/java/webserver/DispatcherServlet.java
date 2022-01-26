package webserver;

import controller.KinaController;
import lombok.extern.slf4j.Slf4j;
import util.HttpRequestUtils;
import webserver.annotation.RequestMethod;
import webserver.model.KinaHttpRequest;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class DispatcherServlet extends Thread {
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
            Map<String, String> queryMap = HttpRequestUtils.parseQueryString(httpRequest.uri().getQuery());
            String uriString = httpRequest.uri().getPath();
            Method method = HandlerMapping.getMethod(RequestMethod.valueOf(httpRequest.method()), uriString);
            if (method == null) { // 정적 파일 요청
                RENDERER.render(dos, httpRequest.uri().getPath());
            } else { // controller 호출
                String result = (String) method.invoke(CONTROLLER, queryMap);
                if (result.startsWith("redirect:")) {
                    RENDERER.redirect(dos, httpRequest.headers().map().get("Host").get(0) + result.split(":")[1]);
                } else {
                    RENDERER.render(dos, result);
                }
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
