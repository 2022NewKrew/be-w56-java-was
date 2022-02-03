package framework;

import framework.util.RequestMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * FrontController로부터 요청을 전달받아 해당 요청을 매핑한 컨트롤러가 있는지 검색
 */
public class HandlerMapping {
    private final Map<Method, Object> methodObjectMap;

    public HandlerMapping(Map<Method, Object> methodObjectMap) {
        this.methodObjectMap = methodObjectMap;
    }

    public String requestToController(HttpRequest request) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return invokeMatchedMethod(request);
    }

    public String invokeMatchedMethod(HttpRequest request) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Method matchedMethod = getMatchedMethod(request);
        Object controllerInstance = methodObjectMap.get(matchedMethod);
        return (String) matchedMethod.invoke(controllerInstance, request);
    }

    private Method getMatchedMethod(HttpRequest request) throws IllegalAccessException {
        String url = request.getUrl();
        String httpMethod = request.getMethod();
        var methodSet = methodObjectMap.keySet();

        return methodSet.stream()
                .filter(method -> this.isMatchedMethod(method, url, httpMethod))
                .findAny()
                .orElseThrow(() -> new IllegalAccessException(String.format("존재하지 않는 페이지입니다. %s", request.getUrl())));
    }

    private boolean isMatchedMethod(Method method, String url, String httpMethod) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String mappingUrl = requestMapping.value();
        String mappingMethod = String.valueOf(requestMapping.method());
        return Pattern.matches(mappingUrl, url) && mappingMethod.equals(httpMethod);
    }
}
