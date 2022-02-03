package controller.adapter;

import constant.GlobalConfig;
import controller.RequestUrlController;
import controller.annotation.RequestMapping;
import http.header.HttpHeaders;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.ModelAndView;
import http.status.HttpStatus;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class UrlMappingHandlerAdapter implements HandlerAdapter {
    private static final Class<RequestUrlController> requestUrlControllerClass = RequestUrlController.class;
    public static final Pattern REGEX_TEMPLATE_VARS = Pattern.compile("\\{\\{(\\w+)\\}\\}");

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
            ModelAndView mv = invokeHandlerMethod(request, response, handlerMethod);
            if (mv.isRedirect()) {
                redirect(request, response, mv);
                return;
            }
            resolve(response, mv);
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

    private ModelAndView invokeHandlerMethod(HttpRequest request, HttpResponse response, Method handlerMethod) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RequestUrlController requestUrlController = requestUrlControllerClass.getConstructor().newInstance();
        Object methodReturn = handlerMethod.invoke(requestUrlController, request, response);
        if (!(methodReturn instanceof ModelAndView)) {
            throw new IllegalStateException("invalid return type");
        }
        return (ModelAndView) methodReturn;
    }

    private void redirect(HttpRequest request, HttpResponse response, ModelAndView mv) {
        String redirectUrl = mv.getRedirectUrl();
        response.status(HttpStatus.FOUND);
        response.addHeader(HttpHeaders.LOCATION, redirectUrl);
    }

    private void resolve(HttpResponse response, ModelAndView mv) throws IOException {
        Path path = Paths.get(GlobalConfig.WEB_ROOT + mv.getViewName() + GlobalConfig.SUFFIX);
        String html = Files.readString(path);
        if (mv.hasModel()) {
            html = applyModelInView(mv, html);
        }
        response.body(html.getBytes(StandardCharsets.UTF_8));
        response.addHeader(HttpHeaders.CONTENT_TYPE, "text/html;charset=utf-8");
    }

    private String applyModelInView(ModelAndView mv, String html) {
        Matcher matcher = REGEX_TEMPLATE_VARS.matcher(html);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String key = matcher.group(1);
            matcher.appendReplacement(sb, mv.getAttr(key));
        }
        matcher.appendTail(sb);
        html = sb.toString();
        return html;
    }
}
