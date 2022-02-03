package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ReflectionUtils;
import util.annotation.RequestMapping;
import webserver.controller.Controller;
import webserver.controller.StaticFileController;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HandlerMapper {
    private static final Logger log = LoggerFactory.getLogger(HandlerMapper.class);
    private static final StaticFileController staticFileController = StaticFileController.getInstance();


    public String map(Request request) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for(Class cls : ReflectionUtils.findAllClasses("webserver.controller")){
            for(Method method : Arrays.asList(cls.getDeclaredMethods())){
                RequestMapping annotation = method.getDeclaredAnnotation(RequestMapping.class);
                if(annotation!=null && request.getUri().equals(annotation.value()) && request.getMethod().equals(annotation.method())){
                    return (String)method.invoke(cls.getMethod("getInstance").invoke(null), request);
                }
            }
        }
        return staticFileController.control(request);
    }
}
