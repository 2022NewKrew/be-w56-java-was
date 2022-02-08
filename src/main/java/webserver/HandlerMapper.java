package webserver;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ReflectionUtils;
import util.annotation.RequestMapping;
import webserver.controller.StaticFileController;
import webserver.view.ModelAndView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerMapper {
    private static final Logger log = LoggerFactory.getLogger(HandlerMapper.class);
    private static final StaticFileController staticFileController = StaticFileController.getInstance();

    public ModelAndView map(Request request, Response response) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for(Class cls : ReflectionUtils.findAllClasses("webserver.controller")){
            for(Method method : cls.getDeclaredMethods()){
                RequestMapping annotation = method.getDeclaredAnnotation(RequestMapping.class);
                if(annotation!=null && StringUtils.equals(request.getUri(),annotation.value()) && StringUtils.equals(request.getMethod(),annotation.method())){
                    return (ModelAndView)method.invoke(cls.getMethod("getInstance").invoke(null), request, response);
                }
            }
        }
        return staticFileController.control(request, response);
    }
}
