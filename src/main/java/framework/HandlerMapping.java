package framework;

import framework.util.RequestMapping;
import mvc.controller.Controller;

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
    private final Controller resourceController;
    private final List<Controller> controllerList;
    private Method matchedMethod;

    public HandlerMapping(Controller resourceController, List<Controller> controllerList) {
        this.resourceController = resourceController;
        this.controllerList = controllerList;
    }

    public String requestToController(HttpRequest requestInfo) throws InvocationTargetException, IllegalAccessException {
        if (requestInfo.isStaticResource()) {
            return requestToResourceController(requestInfo);
        }
        return requestToSpecificController(requestInfo);
    }

    /**
     * static resource 파일 요청에 매핑된 메서드를 호출
     */
    public String requestToResourceController(HttpRequest request) throws IllegalAccessException, InvocationTargetException {
        Arrays.stream(resourceController.getClass().getMethods())
                .filter(method -> setMatchedStaticResourceMethod(request.getUrl(), method))
                .findAny().orElseThrow(() -> new IllegalAccessException("리소스를 찾을 수 없습니다."));

        return (String) matchedMethod.invoke(resourceController, request.getUrl());
    }

    /**
     * 확장자를 가지지 않는 url 요청에 매핑된 메서드 호출
     */
    public String requestToSpecificController(HttpRequest request) throws IllegalAccessException, InvocationTargetException {
        Controller controller = findController(request.getUrl(), request.getMethod())
                    .orElseThrow(() -> new IllegalAccessException(String.format("페이지를 찾을 수 없습니다. %s", request)));

        return (String) matchedMethod.invoke(controller, request);
    }

    private Optional<Controller> findController(String url, String method) {
        return controllerList.stream()
                .filter(controller -> isMatchedClass(controller, url, method))
                .findAny();
    }

    private boolean isMatchedClass(Controller controller, String url, String httpMethod) {
        return Arrays.stream(controller.getClass().getMethods())
                .anyMatch(method -> setMatchedMethod(method, url, httpMethod));
    }

    /**
     * request에 일치하는 method, url을 가진 method를 찾을 시 matchedMethod를 해당 method로 할당해주고 찾았다는 표시로 true를 리턴
     */
    private boolean setMatchedMethod(Method method, String url, String httpMethod) {
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

    private boolean setMatchedStaticResourceMethod(String url, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) return false;

        if(!Pattern.matches(requestMapping.value(), url)) {
            matchedMethod = method;
            return true;
        }
        return false;
    }
}
