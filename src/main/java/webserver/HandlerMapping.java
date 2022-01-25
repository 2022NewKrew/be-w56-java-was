package webserver;

import controller.Controller;
import controller.ResourceController;
import controller.UserController;
import util.RequestMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * FrontController로부터 요청을 전달받아 해당 요청을 매핑한 컨트롤러가 있는지 검색
 */
public class HandlerMapping {
    private Method matchedMethod;
    private static final Controller resourceController = new ResourceController();
    private static final List<Controller> controllerList = Arrays.asList(
            new UserController());

    public String requestToController(RequestInfo requestInfo) throws InvocationTargetException, IllegalAccessException {
        if (requestInfo.isStaticResource()) {
            return requestToResourceController(requestInfo);
        }
        return requestToSpecificController(requestInfo);
    }

    public String requestToResourceController(RequestInfo requestInfo) throws IllegalAccessException, InvocationTargetException {
        Arrays.stream(resourceController.getClass().getMethods())
                .filter(method -> isMatchedStaticResourceMethod(requestInfo.getUrl(), method))
                .findAny().orElseThrow(() -> new IllegalAccessException("리소스를 찾을 수 없습니다."));

        return (String) matchedMethod.invoke(resourceController, requestInfo.getUrl());
    }

    public String requestToSpecificController(RequestInfo requestInfo) throws IllegalAccessException, InvocationTargetException {
        Controller controller = findController(requestInfo.getUrl(), requestInfo.getMethod())
                    .orElseThrow(() -> new IllegalAccessException(String.format("페이지를 찾을 수 없습니다. %s", requestInfo.getUrl())));

        return (String) matchedMethod.invoke(controller);
    }

    private Optional<Controller> findController(String url, String method) {
        return controllerList.stream()
                .filter(controller -> isMatchedClass(controller, url, method))
                .findAny();
    }

    private boolean isMatchedClass(Controller controller, String url, String httpMethod) {
        return Arrays.stream(controller.getClass().getMethods())
                .anyMatch(method -> isMatchedMethod(method, url, httpMethod));
    }

    private boolean isMatchedMethod(Method method, String url, String httpMethod) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) return false;

        String mappingUrl = requestMapping.value();
        String mappingMethod = String.valueOf(requestMapping.method());
        if (mappingUrl.equals(url) && mappingMethod.equals(httpMethod)) {
            matchedMethod = method;
            return true;
        }
        return false;
    }

    private boolean isMatchedStaticResourceMethod(String url, Method method) {
        if(method != null && !Pattern.matches(method.getAnnotation(RequestMapping.class).value(), url)) {
            matchedMethod = method;
            return true;
        }
        return false;
    }
}
