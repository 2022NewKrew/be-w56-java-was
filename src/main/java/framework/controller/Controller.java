package framework.controller;

import framework.util.annotation.RequestMapping;
import framework.util.exception.ClassNotFoundException;
import framework.util.exception.MethodNotFoundException;
import framework.webserver.HttpRequestHandler;
import framework.webserver.HttpResponseHandler;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.Scanners;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

import static framework.util.Constants.CONTROLLER_PACKAGE;

/**
 * Controller 클래스가 구현할 인터페이스
 */
public interface Controller {
    /**
     * default 메소드, 받은 요청 정보에 맞는 메소드를 현재 Controller 객체에서 찾아 실행 후 그 데이터를 반환해주는 메소드
     * @param request Client로부터 받은 요청 정보
     * @param response Client에게 응답해줄 정보
     * @return 메소드 실행 후 반환된 데이터, String형 또는 ModelView형만 가능
     */
    default Object process(HttpRequestHandler request, HttpResponseHandler response) {
        Class<?> currentClass = getClass();

        Controller currentInstance = null;

        try {
            currentInstance = (Controller) currentClass.getMethod("getInstance").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ClassNotFoundException();
        }

        Method method = findMethod(request.getUri(), request.getRequestMethod());

        return invokeMethod(currentInstance, method, request, response);
    }

    /**
     * default 메소드, 받은 URI와 요청 방식에 맞는 메소드를 현재 Controller에서 찾아주는 메소드
     * @param uri 받은 요청 정보의 URI
     * @param requestMethod 요청 방식
     * @param currentClass 현재 Controller 클래스
     * @return 찾은 메소드
     */
    default Method findMethod(String uri, String requestMethod) {
        Reflections reflections = new Reflections(CONTROLLER_PACKAGE, Scanners.MethodsAnnotated);
        Set<Method> methods = reflections.getMethodsAnnotatedWith(RequestMapping.class);

        return methods.stream()
                .filter(m -> {
                    RequestMapping requestPath = m.getAnnotation(RequestMapping.class);
                    return uri.endsWith(requestPath.value()) &&
                            requestPath.requestMethod().toUpperCase(Locale.ROOT).equals(requestMethod);
                })
                .findFirst()
                .orElseThrow(MethodNotFoundException::new);
    }

    /**
     * 현재 Controller 객체 내에서 찾은 메소드를 호출시켜 그 값을 반환해주는 메소드
     * @param currentInstance 현재 Controller 객체
     * @param method Controller 객체에서 호출할 메소드
     * @param request Client로부터 받은 요청 정보
     * @param response Client에게 응답해줄 정보
     * @return 메소드 실행 후 반환된 데이터, String형 또는 ModelView형만 가능
     */
    default Object invokeMethod(Controller currentInstance, Method method, HttpRequestHandler request, HttpResponseHandler response) {
        // 메소드의 매개변수에 따라 호출
        try {
            // HttpRequestHandler, HttpResponseHandler 모두 받는 메소드
            return method.invoke(currentInstance, request, response);
        } catch (IllegalArgumentException e1) {
            try {
                // HttpRequestHandler만 받는 메소드
                return method.invoke(currentInstance, request);
            } catch (IllegalArgumentException e2) {
                try {
                    // HttpResponseHandler만 받는 메소드
                    return method.invoke(currentInstance, response);
                } catch (IllegalArgumentException e3) {
                    try {
                        // 아무 매개변수도 받지 않는 메소드
                        return method.invoke(currentInstance);
                    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                        throw new MethodNotFoundException();
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new MethodNotFoundException();
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new MethodNotFoundException();
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MethodNotFoundException();
        }
    }
}
