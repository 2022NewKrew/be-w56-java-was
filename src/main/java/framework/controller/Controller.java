package framework.controller;

import framework.util.annotation.RequestMapping;
import framework.webserver.HttpRequestHandler;
import framework.webserver.HttpResponseHandler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;

/**
 * Controller 클래스가 구현할 인터페이스
 */
public interface Controller {
    /**
     * default 메소드, 받은 요청 정보에 맞는 메소드를 현재 Controller 객체에서 찾아 실행 후 그 데이터를 반환해주는 메소드
     * @param request Client로부터 받은 요청 정보
     * @param response Client에게 응답해줄 정보
     * @return 메소드 실행 후 반환된 데이터, String형 또는 ModelView형만 가능
     * @throws Exception: 원하는 메소드를 실행하지 못했을 때 발생
     */
    default Object process(HttpRequestHandler request, HttpResponseHandler response) throws Exception {
        Class<?> currentClass = getClass();

        Controller currentInstance = (Controller) currentClass.getMethod("getInstance").invoke(null);
        Method method = findMethod(request.getUri(), request.getRequestMethod(), currentClass);

        try {
            return method.invoke(currentInstance, request, response);
        } catch (IllegalArgumentException e1) {
            try {
                return method.invoke(currentInstance, request);
            } catch (IllegalArgumentException e2) {
                return method.invoke(currentInstance);
            }
        }
    }

    /**
     * default 메소드, 받은 URI와 요청 방식에 맞는 메소드를 현재 Controller에서 찾아주는 메소드
     * @param uri 받은 요청 정보의 URI
     * @param requestMethod 요청 방식
     * @param currentClass 현재 Controller 클래스
     * @return 찾은 메소드
     */
    default Method findMethod(String uri, String requestMethod, Class<?> currentClass) {
        return Arrays.stream(currentClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .filter(m -> {
                    RequestMapping requestPath = m.getAnnotation(RequestMapping.class);
                    return uri.endsWith(requestPath.value()) &&
                            requestPath.requestMethod().toUpperCase(Locale.ROOT).equals(requestMethod);
                })
                .findFirst()
                .orElseThrow();
    }
}
