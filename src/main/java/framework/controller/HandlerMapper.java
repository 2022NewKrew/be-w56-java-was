package framework.controller;

import framework.container.Container;
import framework.util.HttpSession;
import framework.util.annotation.RequestMapping;
import framework.util.exception.ClassNotFoundException;
import framework.util.exception.MethodNotFoundException;
import framework.util.exception.WrongReturnTypeException;
import framework.view.ModelView;
import framework.webserver.HttpRequestHandler;
import framework.webserver.HttpResponseHandler;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Locale;
import java.util.Set;

import static framework.util.Constants.CONTROLLER_PACKAGE;
import static framework.util.Constants.REDIRECT_MARK;

/**
 * Handler Mapper,
 * 알맞은 Controller를 찾아주는 역할
 */
public class HandlerMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerMapper.class);

    /**
     * 받은 요청 정보에 맞는 Controller 클래스를 찾고 해당 Controller로부터 받은 데이터를 ModelView로 반환해주는 메소드
     * @param request Client로부터 받은 요청 정보
     * @param response Client에게 응답해줄 정보
     * @param modelView View에 전달해야 할 정보들을 담을 객체
     * @return Controller로부터 받은 데이터 (null (void일 때), String형 객체, ModelView형 객체)
     */
    public static void handle(HttpRequestHandler request, HttpResponseHandler response, ModelView modelView) {
        Object object = process(request, response, modelView);

        // Controller로부터 받은 데이터가 없을 경우 (void)
        if (object == null) {
            return;
        }

        // Controller로부터 받은 데이터가 String일 경우
        if (object instanceof String) {
            String uri = (String) object;

            // 반환 받은 String에 "redirect:"가 붙어있다면 Static file X
            modelView.setStatic(!uri.startsWith(REDIRECT_MARK));

            modelView.setUri(uri);
            return;
        }

        // Controller로부터 받은 데이터가 ModelView일 경우
        if (object instanceof ModelView) {
            // 해당 내용을 복사
            modelView.copy((ModelView) object);
            return;
        }

        // 위 세 형태가 아니라면 원하는 반환 타입이 아니기 때문에 예외 발생
        throw new WrongReturnTypeException();
    }

    /**
     * 받은 요청 정보에 맞는 Controller 클래스를 찾고 해당 Controller로부터 받은 데이터를 반환해주는 메소드
     * @param request Client로부터 받은 요청 정보
     * @param response Client에게 응답해줄 정보
     * @param modelView View에 전달해야 할 정보들을 담을 객체
     * @return Controller로부터 받은 데이터 (null (void일 때), String형 객체, ModelView형 객체)
     */
    private static Object process(HttpRequestHandler request, HttpResponseHandler response, ModelView modelView) {
        String uri = request.getUri();
        String requestMethod = request.getRequestMethod();

        // Controller를 찾아서 process 메소드 호출
        Object controller = findController(uri);
        Method method = findMethod(controller, requestMethod, uri);
        Object object = invokeMethod(controller, method, request, response, modelView);

        // String형이나 ModelView형이 아니라면 예외 발생
        if (!(object instanceof String || object instanceof ModelView)) {
            throw new IllegalArgumentException();
        }

        return object;
    }

    /**
     * 받은 URI에 맞는 Controller 클래스 객체를 반환해주는 메소드
     * @param uri 받은 요청 정보의 URI
     * @return URI에 맞는 Controller 객체
     */
    private static Object findController(String uri) {
        // RequestPath 어노테이션이 있는 클래스 확인
        Reflections reflections = new Reflections(CONTROLLER_PACKAGE);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(RequestMapping.class);

        // 받은 URI의 첫 부분에 해당하는 Controller 클래스 확인
        Class<?> subControllerClass = annotated.stream()
                .filter(c -> uri.startsWith(c.getAnnotation(RequestMapping.class).value()))
                .findFirst()
                .orElseThrow(ClassNotFoundException::new);

        return Container.getInstanceFromContainer(subControllerClass);
    }

    /**
     * 받은 URI와 요청 방식에 맞는 메소드를 현재 Controller에서 찾아주는 메소드
     * @param controller 현재 Controller 객체
     * @param uri 받은 요청 정보의 URI
     * @param requestMethod 요청 방식
     * @return 찾은 메소드
     */
    private static Method findMethod(Object controller, String requestMethod, String uri) {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forClass(controller.getClass()))
                        .setScanners(Scanners.MethodsAnnotated));

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
     * @param controller 현재 Controller 객체
     * @param method Controller 객체에서 호출할 메소드
     * @param request Client로부터 받은 요청 정보
     * @param response Client에게 응답해줄 정보
     * @param modelView View에 전달해야 할 정보들을 담을 객체
     * @return 메소드 실행 후 반환된 데이터 (null (void일 때), String형 객체, ModelView형 객체)
     */
    private static Object invokeMethod(Object controller, Method method, HttpRequestHandler request,
                                       HttpResponseHandler response, ModelView modelView) {
        // 현재 메소드에 넘겨줘야 하는 매개변수들
        Parameter[] parameters = method.getParameters();
        int size = parameters.length;

        // 현재 메소드에 넘겨줄 인자들 (객체들)
        Object[] arguments = new Object[size];

        // 현재 인자에 맞게 넣어줌
        for (int i = 0; i < size; i++) {
            String className = parameters[i].getType().getName();

            if (className.equals(request.getClass().getName())) {
                arguments[i] = request;
            }

            if (className.equals(HttpSession.class.getName())) {
                arguments[i] = request.getSession();
            }

            if (className.equals(response.getClass().getName())) {
                arguments[i] = response;
            }

            if (className.equals(modelView.getClass().getName())) {
                arguments[i] = modelView;
            }
        }

        // 메소드 호출
        try {
            return method.invoke(controller, arguments);
        } catch (Exception e) {
            throw new MethodNotFoundException();
        }
    }
}
