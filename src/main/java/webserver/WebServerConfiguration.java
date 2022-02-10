package webserver;

import annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ClassUtils;
import webserver.view.TemplateView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Set;

public class WebServerConfiguration {
    private static final Logger log = LoggerFactory.getLogger(WebServerConfiguration.class);

    public static void doConfigure() {
        initializeStereoTypes();
        initializeDispatcherServlet();
    }

    private static void initializeStereoTypes() {
        registerAnnotatedClasses(Controller.class);
    }

    private static void registerAnnotatedClasses(Class<? extends Annotation> annotation) {
        Set<Class<?>> classes = ClassUtils.findAnnotatedClasses(annotation);
        classes.forEach(WebServerConfiguration::registerClass);
    }

    private static void registerClass(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            Object instance = constructor.newInstance();
            SingletonBeanRegistry.registerBean(clazz.getName(), instance);
        } catch (ReflectiveOperationException e) {
            log.error("Failed to register class of " + clazz.getName());
        }
    }

    private static void initializeDispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.initialize();
        SingletonBeanRegistry.registerBean("DispatcherServlet", dispatcherServlet);
    }
}
