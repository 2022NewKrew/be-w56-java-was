package webserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.MainController;
import lombok.extern.slf4j.Slf4j;
import util.HttpRequestUtils;
import webserver.annotation.RequestMethod;
import webserver.model.HttpStatus;
import webserver.model.RequestMapping;
import webserver.model.WebHttpRequest;
import webserver.model.WebHttpResponse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class RequestHandler {
    private static final RequestHandler INSTANCE = new RequestHandler();

    public static RequestHandler getInstance() {
        return INSTANCE;
    }

    public RequestHandler() {

    }

    private final MainController controller = MainController.getInstance();

    public void handle(WebHttpRequest httpRequest, WebHttpResponse httpResponse) {
        if (!RequestMapping.isRegistered(httpRequest)) {
            httpResponse.setHttpStatus(HttpStatus.OK);
            setContent(httpResponse, httpRequest.uri().getPath());
            return;
        }

        RequestMethod requestMethod = RequestMethod.valueOf(httpRequest.method());
        String uri = httpRequest.uri().getPath();
        Method controllerMethod = RequestMapping.getControllerMethod(requestMethod, uri);
        Object[] params = setParameters(httpRequest, httpResponse, requestMethod, controllerMethod);
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

    private Object[] setParameters(
            WebHttpRequest httpRequest,
            WebHttpResponse httpResponse,
            RequestMethod requestMethod,
            Method controllerMethod) {
        Map<String, String> paramMap = getQueryString(httpRequest, requestMethod);
        paramMap.putAll(getRequestBody(httpRequest, requestMethod));
        Class[] parameters = controllerMethod.getParameterTypes();

        ObjectMapper mapper = new ObjectMapper();
        List<Object> params = new ArrayList<>();
        for (Class type : parameters) {
            if (type == WebHttpRequest.class) {
                params.add(httpRequest);
            } else if (type == WebHttpResponse.class) {
                params.add(httpResponse);
            } else {
                params.add(mapper.convertValue(paramMap, type));
            }
        }
        return params.toArray();
    }

    private Map<String, String> getQueryString(WebHttpRequest httpRequest, RequestMethod requestMethod) {
        return HttpRequestUtils.parseQueryString(httpRequest.uri().getQuery());
    }

    private Map<String, String> getRequestBody(WebHttpRequest httpRequest, RequestMethod requestMethod) {
        return HttpRequestUtils.parseQueryString(httpRequest.getBody());
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
