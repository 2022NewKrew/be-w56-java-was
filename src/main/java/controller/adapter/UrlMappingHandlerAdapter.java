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
import util.ResourceUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class UrlMappingHandlerAdapter implements HandlerAdapter {
    private static final Class<RequestUrlController> requestUrlControllerClass = RequestUrlController.class;
    private static final Pattern REGEX_TEMPLATE_ITER = Pattern.compile("\\{\\{\\#(\\w+)\\}\\}([\\w\\W]*)\\{\\{\\/(\\w+)\\}\\}");
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
//        Path path = Paths.get(getResource(mv.getViewName() + GlobalConfig.SUFFIX));
//        String html = Files.readString(path);
        byte[] resource = ResourceUtils.getResource(mv.getViewName() + GlobalConfig.SUFFIX);
        String html = new String(resource, StandardCharsets.UTF_8);
        if (mv.hasModel()) {
            html = applyModelInView(mv, html);
        }
        response.body(html.getBytes(StandardCharsets.UTF_8));
        response.addHeader(HttpHeaders.CONTENT_TYPE, "text/html;charset=utf-8");
    }

    private String applyModelInView(ModelAndView mv, String html) {
        html = replaceTemplateIter(mv, html);
        return replaceTemplateVars(mv, html);
    }

    private String replaceTemplateIter(ModelAndView mv, String html) {
        StringBuilder sb = new StringBuilder();
        Matcher iterMatcher = REGEX_TEMPLATE_ITER.matcher(html);
        while (iterMatcher.find()) {
            StringBuilder innerSb = new StringBuilder();
            String var = iterMatcher.group(1);
            String innerHtml = iterMatcher.group(2);
            Iterable<?> iter = (Iterable<?>) mv.getAttr(var);
            iter.forEach(obj -> {
                String inner = replaceTemplateIterInner(obj, innerHtml);
                innerSb.append(inner);
            });
            iterMatcher.appendReplacement(sb, innerSb.toString());
        }
        iterMatcher.appendTail(sb);
        return sb.toString();
    }

    private String replaceTemplateIterInner(Object obj, String html) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = REGEX_TEMPLATE_VARS.matcher(html);
        while (matcher.find()) {
            String varName = matcher.group(1);
            String value = getField(obj, varName);
            matcher.appendReplacement(sb, value);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String getField(Object obj, String varName) {
        try {
            Field field = obj.getClass().getDeclaredField(varName);
            field.setAccessible(true);
            return field.get(obj).toString();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException();
        }
    }

    private String replaceTemplateVars(ModelAndView mv, String html) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = REGEX_TEMPLATE_VARS.matcher(html);
        while (matcher.find()) {
            String key = matcher.group(1);
            matcher.appendReplacement(sb, (String) mv.getAttr(key));
        }
        matcher.appendTail(sb);
        html = sb.toString();
        return html;
    }
}
