package webserver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import user.service.UserService;
import webserver.annotation.Controller;
import webserver.annotation.RequestMapping;
import webserver.domain.HttpStatus;
import webserver.domain.Request;
import webserver.domain.Response;

public class ControllerHandler {

    private static final String FILE_ROOT_PATH = "./webapp";
    private static final UserService userService = new UserService();

    public static Response run(Request request)
        throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (request.isFile()) {
            return getFrontResponse(request);
        }
        Set<Class<?>> typesAnnotatedWith = new Reflections(
            new ConfigurationBuilder().forPackage("user")
        ).getTypesAnnotatedWith(Controller.class);

        String httpMethod = request.getHttpMethod();
        String path = request.getPath();

        for (Class<?> controller : typesAnnotatedWith) {
            for (Method method : controller.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    if (!isValidMethod(httpMethod, path, annotation)) {
                        continue;
                    }
                    Object routerObject = controller.getConstructor(UserService.class).newInstance(userService);
                    // TODO: 파라미터 매핑해서 넣어보기
                    return (Response) method.invoke(routerObject, request);
                }
            }
        }
        return Response.createResponse(HttpStatus.BAD_REQUEST, null, null);
    }

    private static boolean isValidMethod(String httpMethod, String path, RequestMapping annotation) {
        if (!annotation.method().equals(httpMethod)) {
            return false;
        }
        if (!annotation.value().equals(path)) {
            return false;
        }
        return true;
    }

    private static Response getFrontResponse(Request request) throws IOException {
        byte[] body = Files.readAllBytes(new File(FILE_ROOT_PATH + request.getPath()).toPath());
        return Response.createResponse(HttpStatus.OK, body, request.getHeaderAttribute("Accept"));
    }
}
