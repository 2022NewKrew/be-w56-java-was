package webserver.controller;

import java.lang.reflect.Method;

import webserver.common.HttpMethod;
import webserver.controller.request.HttpRequest;

public class DispatcherServlet {

    public static Method handlerMapping(HttpRequest httpRequest) throws NoSuchMethodException {
        // Service 클래스 안에 있는 메서드 들을 모두 확인한다.
        Method[] methods = Controller.class.getMethods();

        // 1. 매칭되는 메서드가 있을 경우 반환한다.
        for (Method m : methods) {
            if (m.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping annotation = m.getDeclaredAnnotation(RequestMapping.class);
                if (annotation.method() == httpRequest.getMethod() && annotation.path().equals(httpRequest.getPath())) {
                    return m;
                }
            }
        }

        // 2. 만약 GET 요청이라면 정적 요청 처리 ( 없는 파일을 요청할 경우 404 에러 발생 )
        if (httpRequest.getMethod() == HttpMethod.GET){
            return Controller.class.getMethod("getStatic", HttpRequest.class);
        }

        // 3. GET 요청도 아니라면 405 에러 발생
        return Controller.class.getMethod("err405", HttpRequest.class);
    }
}
