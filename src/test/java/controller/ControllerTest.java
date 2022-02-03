package controller;

import controller.annotation.RequestMapping;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ControllerTest {

    @Test
    public void annotationTest() {
        Class<RequestUrlController> requestUrlControllerClass = RequestUrlController.class;
        Arrays.stream(requestUrlControllerClass.getDeclaredMethods())
                .forEach(method -> {
                    String name = method.getName();
                    System.out.println("name = " + name);
                    boolean annotationPresent = method.isAnnotationPresent(RequestMapping.class);
                    System.out.println("annotationPresent = " + annotationPresent);
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    String url = annotation.value();
                    System.out.println("url = " + url);
                    String met = annotation.method();
                    System.out.println("met = " + met);
                    if (name.equals("index")) {
                        try {
                            Constructor<RequestUrlController> constructor = requestUrlControllerClass.getConstructor();
                            RequestUrlController requestUrlController = constructor.newInstance();
                            Object invoke = method.invoke(requestUrlController, (Object) null);
                            System.out.println("invoke = " + invoke);
                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
