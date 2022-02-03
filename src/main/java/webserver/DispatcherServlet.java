package webserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.MainController;
import lombok.extern.slf4j.Slf4j;
import util.HttpRequestUtils;
import webserver.annotation.RequestMethod;
import webserver.model.HttpStatus;
import webserver.model.WebHttpRequest;
import webserver.model.WebHttpResponse;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
            WebHttpResponse httpResponse = WebHttpResponse.of(httpRequest);
            log.info(httpRequest.toString());
            invoke(httpRequest, httpResponse);
            resolver.resolve(httpRequest, httpResponse, dos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void invoke(WebHttpRequest httpRequest, WebHttpResponse httpResponse) {
        if (!HandlerMapping.isRegistered(httpRequest)) {
            httpResponse.setHttpStatus(HttpStatus.OK);
            setContent(httpResponse, httpRequest.uri().getPath());
            return;
        }
        RequestMethod requestMethod = RequestMethod.valueOf(httpRequest.method());
        String uri = httpRequest.uri().getPath();
        Method controllerMethod = HandlerMapping.getControllerMethod(requestMethod, uri);
        Map<String, String> map = null;
        if (requestMethod == RequestMethod.GET)
            map = HttpRequestUtils.parseQueryString(httpRequest.uri().getQuery());
        else
            map = HttpRequestUtils.parseQueryString(httpRequest.getBody());
        log.info(httpRequest.toString());
        int parameterCount = controllerMethod.getParameterCount();
        Class[] parameters = controllerMethod.getParameterTypes();

        ObjectMapper mapper = new ObjectMapper();
        Object[] params = new Object[parameterCount];
        for (int index = 0; index < parameterCount; index++) {
            if (parameters[index] == WebHttpRequest.class) {
                params[index] = httpRequest;
            } else if (parameters[index] == WebHttpResponse.class) {
                params[index] = httpResponse;
            } else {
                params[index] = mapper.convertValue(map, parameters[index]);
            }
        }
        try {
            String result = (String) controllerMethod.invoke(controller, params);
            if (result.startsWith("redirect:")) {
                httpResponse.setHttpStatus(HttpStatus.FOUND);
                httpResponse.setHeaders("Location", result.split(":")[1]);
                return;
            }
            httpResponse.setHttpStatus(HttpStatus.OK);
            setContent(httpResponse, result);
        } catch (InvocationTargetException | IllegalAccessException e) {
            httpResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void setContent(WebHttpResponse httpResponse, String pathString) {
        Path path = new File("./webapp" + pathString).toPath();
        try {
            byte[] body = Files.readAllBytes(path);
            String extension = pathString.substring(pathString.lastIndexOf("."));
            Path tmpPath = new File("file" + extension).toPath();
            String mimeType = Files.probeContentType(tmpPath);
            if (mimeType == null) {
                if (extension.equals(".woff")) {
                    mimeType = "application/font-woff";
                } else {
                    mimeType = "text/html";
                }
            }
            httpResponse.setHeaders("Content-Type", mimeType + ";charset=utf-8");
            httpResponse.setHeaders("Content-Length", Integer.toString(body.length));
            httpResponse.setBody(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
