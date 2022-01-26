package controller.adapter;

import constant.GlobalConfig;
import controller.annotation.RequestMapping;
import controller.RequestUrlController;
import http.header.HttpHeaders;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.status.HttpStatus;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
public class UrlMappingHandlerAdapter implements HandlerAdapter {
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
        Class<RequestUrlController> requestUrlControllerClass = RequestUrlController.class;

        Method handlerMethod = Arrays.stream(requestUrlControllerClass.getDeclaredMethods())
                .filter(method -> {
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    return annotation.value().equals(request.getUrl()) && annotation.method().equals(request.getMethod());
                })
                .findAny()
                .orElseThrow();

        try {
            RequestUrlController requestUrlController = requestUrlControllerClass.getConstructor().newInstance();
            Object methodReturn = handlerMethod.invoke(requestUrlController, request, response);
            if (!(methodReturn instanceof String)) {
                throw new IllegalStateException("invalid return type");
            }
            String viewName = (String) methodReturn;
            if (viewName.contains("redirect:")) {
                // redirect
                String redirectUrl = viewName.replace("redirect:", "");
                response.status(HttpStatus.FOUND);
                response.addHeader("Location", request.getHeader("Origin") + redirectUrl);
                return;
            }
            String url = GlobalConfig.WEB_ROOT + viewName + GlobalConfig.SUFFIX;
            byte[] body = Files.readAllBytes(Paths.get(url));
            response.body(body);
            response.addHeader(HttpHeaders.CONTENT_TYPE, "text/html;charset=utf-8");
        } catch (IllegalAccessException | InvocationTargetException | IOException exception) {
            log.error(exception.getMessage());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
