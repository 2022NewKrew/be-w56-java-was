package webserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.MainController;
import lombok.extern.slf4j.Slf4j;
import util.HttpRequestUtils;
import webserver.annotation.RequestMethod;
import webserver.model.WebHttpRequest;
import webserver.model.WebHttpResponse;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class DispatcherServlet extends Thread {
    private final MainController controller = MainController.getInstance();
    private final ViewResolver resolver = ViewResolver.getInstance();
    private final Socket connection;

    public DispatcherServlet(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            DataOutputStream dos = new DataOutputStream(out);

            WebHttpRequest httpRequest = WebHttpRequest.of(buffer);
            WebHttpResponse httpResponse = WebHttpResponse.of(dos);

            invoke(httpRequest, httpResponse);
            resolver.resolve(httpRequest, httpResponse);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void invoke(WebHttpRequest httpRequest, WebHttpResponse httpResponse) {
        if (!HandlerMapping.isRegistered(httpRequest))
            return;
        RequestMethod requestMethod = RequestMethod.valueOf(httpRequest.method());
        String uri = httpRequest.uri().getPath();
        String queryString = httpRequest.uri().getQuery();
        Method controllerMethod = HandlerMapping.getControllerMethod(requestMethod, uri);
        Map<String, String> map = HttpRequestUtils.parseQueryString(queryString);

        int parameterCount = controllerMethod.getParameterCount();
        Class[] parameters = controllerMethod.getParameterTypes();

        ObjectMapper mapper = new ObjectMapper();
        Object[] params = new Object[parameterCount];
        for (int index = 0; index < parameterCount; index++) {
            params[index] = mapper.convertValue(map, parameters[index]);
        }
        try {
            String result = (String) controllerMethod.invoke(controller, params);
            httpResponse.setResult(result);
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.error(e.getMessage());
        }
    }
}
