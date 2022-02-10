package util.annotation;

import controller.Controller;
import controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AnnotationHandler {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandler.class);

    public static void handle(Controller controller) {

//        try {
//            Method[] methods = controller.getClass().getDeclaredMethods();
//            log.debug(methods[0].toString());
//        log.debug(Boolean.toString(controller.getClass().isAnnotationPresent(Auth.class)));
//        Method method = controller.getClass().getDeclaredMethods()[0];
//        log.debug(Boolean.toString(method.isAnnotationPresent(Auth.class)));
//        } catch (NoSuchMethodException e) {
//            log.error(e.getMessage());
//        }

//        try {
//            String className = controller.getClass().getName().split("\\$\\$")[0];
//            Class<?> clazz = Class.forName(className);
//        } catch (ClassNotFoundException e) {
//            log.error(e.getMessage());
//            throw new RuntimeException(e.getMessage());
//        }
//
        Field[] fields = UserController.class.getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields).filter(field -> field.isAnnotationPresent(Auth.class))
                        .collect(Collectors.toList());
        log.debug(fieldList.toString());
        Field field = fieldList.get(0);
        log.debug(field.getDeclaringClass().toString());
        log.debug(field.getClass().toString());
//        log.debug(controller.toString());
//        log.debug(field.toString());

//        log.debug(Boolean.toString(controller.getClass().isAnnotationPresent(Auth.class)));
    }
}
