package framework.controller;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import framework.util.annotation.RequestMapping;
import framework.view.ModelView;
import framework.webserver.HttpRequestHandler;
import framework.webserver.HttpResponseHandler;

import java.lang.reflect.Method;
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
     * @return Controller로부터 받은 데이터를 담은 ModelView
     * @throws Exception 원하는 Controller 클래스를 찾지 못하거나 원하는 메소드를 찾지 못할 때 발생
     */
    public static ModelView handle(HttpRequestHandler request, HttpResponseHandler response) throws Exception {
        Object object = process(request, response);

        if (object instanceof String) {
            String uri = (String) object;
            return ModelView.builder()
                    .isStatic(uri.startsWith(REDIRECT_MARK))
                    .uri(uri)
                    .build();
        }

        return (ModelView) object;
    }

    /**
     * 받은 요청 정보에 맞는 Controller 클래스를 찾고 해당 Controller로부터 받은 데이터를 반환해주는 메소드
     * @param request Client로부터 받은 요청 정보
     * @param response Client에게 응답해줄 정보
     * @return Controller로부터 받은 데이터
     * @throws Exception 원하는 Controller 클래스를 찾지 못하거나 원하는 메소드를 찾지 못할 때 발생
     */
    private static Object process(HttpRequestHandler request, HttpResponseHandler response) throws Exception {
        String uri = request.getUri();

        // Controller를 찾아서 process 메소드 호출
        Controller controller = findController(uri);
        Object object = controller.process(request, response);

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
     * @throws Exception 원하는 Controller 클래스를 찾지 못하거나 원하는 메소드를 찾지 못할 때 발생
     */
    private static Controller findController(String uri) throws Exception {
        // RequestPath 어노테이션이 있는 클래스 확인
        Reflections reflections = new Reflections(CONTROLLER_PACKAGE);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(RequestMapping.class);

        // 받은 URI의 첫 부분에 해당하는 컨트롤러 클래스 확인
        Class<?> subControllerClass = annotated.stream()
                .filter(c -> uri.startsWith(c.getAnnotation(RequestMapping.class).value()))
                .findFirst()
                .orElseThrow();

        // 해당 컨트롤러의 객체를 들고와서 처리
        Method method = subControllerClass.getMethod("getInstance");
        return (Controller) method.invoke(null);
    }
}
