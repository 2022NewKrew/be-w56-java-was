package infrastructure.dispatcher;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import application.controller.Controller;
import http.common.HttpMethod;
import http.request.HttpRequest;
import infrastructure.dto.AppResponse;
import http.response.HttpResponse;
import infrastructure.staticservice.StaticController;
import infrastructure.view.View;

public class DispatcherServlet {

    public static HttpResponse handle(HttpRequest httpRequest)
            throws IOException, URISyntaxException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {

        Method method = handlerMapping(httpRequest);
        AppResponse appResponse = handlerAdapt(method, httpRequest);
        HttpResponse httpResponse = View.render(appResponse);

        return httpResponse;
    }

    private static Method handlerMapping(HttpRequest httpRequest) throws NoSuchMethodException {

        Method[] methods = Controller.class.getMethods();

        // 1. application 에 요청에 대응하는 메서드가 있을 경우 그 메서드를 반환한다.
        for (Method m : methods) {
            if (m.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping annotation = m.getDeclaredAnnotation(RequestMapping.class);
                if (annotation.method() == httpRequest.getMethod() && annotation.path().equals(httpRequest.getPath())) {
                    return m;
                }
            }
        }

        // 2. application 에 요청에 대응되는 메서드가 없고 GET 요청이라면 정적 파일 요청으로 처리한다.
        if (httpRequest.getMethod() == HttpMethod.GET) {
            return StaticController.class.getMethod("getStatic", HttpRequest.class);
        }

        // 3. 유저 요청에 대응하는 메서드도 없고 GET 요청도 아니라면 405 에러를 발생시킨다.
        return StaticController.class.getMethod("err405", HttpRequest.class);
    }

    private static AppResponse handlerAdapt(Method method, HttpRequest httpRequest)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> declaringClass = method.getDeclaringClass();
        Constructor<?> declaredConstructor = declaringClass.getDeclaredConstructor();
        Object o = declaredConstructor.newInstance();
        AppResponse appResponse = (AppResponse) method.invoke(o, httpRequest);
        return appResponse;
    }
}
