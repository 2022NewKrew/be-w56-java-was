package controller.adapter;

import constant.GlobalConfig;
import controller.RequestUrlController;
import controller.annotation.RequestMapping;
import http.header.HttpHeaders;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.status.HttpStatus;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
public class UrlMappingHandlerAdapter implements HandlerAdapter {
    private static final Class<RequestUrlController> requestUrlControllerClass = RequestUrlController.class;

    @Override
    public boolean supports(HttpRequest request) {
        Class<RequestUrlController> requestUrlControllerClass = RequestUrlController.class;
        return Arrays.stream(requestUrlControllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .anyMatch(method -> {
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    return annotation.value().equals(request.getUrl()) && annotation.method().equals(request.getMethod());
                });
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        Method handlerMethod = findHandlerMethod(request);

        try {
            String viewName = invokeHandlerMethod(request, response, handlerMethod);
            if (viewName.contains("redirect:")) {
                redirect(request, response, viewName);
                return;
            }
            ok(response, viewName);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    private Method findHandlerMethod(HttpRequest request) {
        return Arrays.stream(requestUrlControllerClass.getDeclaredMethods())
                .filter(method -> {
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    return annotation.value().equals(request.getUrl()) && annotation.method().equals(request.getMethod());
                })
                .findAny()
                .orElseThrow();
    }

    private String invokeHandlerMethod(HttpRequest request, HttpResponse response, Method handlerMethod) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RequestUrlController requestUrlController = requestUrlControllerClass.getConstructor().newInstance();
        Object methodReturn = handlerMethod.invoke(requestUrlController, request, response);
        if (!(methodReturn instanceof String)) {
            throw new IllegalStateException("invalid return type");
        }
        return (String) methodReturn;
    }

    private void redirect(HttpRequest request, HttpResponse response, String viewName) {
        String redirectUrl = viewName.replace("redirect:", "");
        response.status(HttpStatus.FOUND);
        response.addHeader(HttpHeaders.LOCATION, redirectUrl);
    }

    private void ok(HttpResponse response, String viewName) throws IOException {
        String url = GlobalConfig.WEB_ROOT + viewName + GlobalConfig.SUFFIX;
        byte[] body = Files.readAllBytes(Paths.get(url));
        response.body(body);
        response.addHeader(HttpHeaders.CONTENT_TYPE, "text/html;charset=utf-8");
    }
}
