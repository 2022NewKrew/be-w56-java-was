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

public class HandlerMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerMapper.class);

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

    private static Object process(HttpRequestHandler request, HttpResponseHandler response) throws Exception {
        String uri = request.getUri();

        Controller controller = findController(uri);
        Object object = controller.process(request, response);

        if (!(object instanceof String || object instanceof ModelView)) {
            throw new IllegalArgumentException();
        }

        return object;
    }

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
