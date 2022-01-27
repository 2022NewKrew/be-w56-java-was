package com.leoserver.webserver;

import com.leoserver.webserver.annotation.Autowired;
import com.leoserver.webserver.annotation.Component;
import com.leoserver.webserver.handler.ServletHandler;
import com.leoserver.webserver.handler.StaticResourceHandler;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationContext {

  private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

  private final StaticResourceHandler staticResourceHandler;
  private final ServletHandler servletHandler;
  private Set<Object> beans;
  private final Reflections reflections = new Reflections("com.leoserver", Scanners.TypesAnnotated,
      Scanners.FieldsAnnotated);

  public ApplicationContext() throws Exception {
    initialize();
    this.servletHandler = new ServletHandler(this);
    this.staticResourceHandler = new StaticResourceHandler();
  }


  private void initialize() throws Exception {
    beans = new HashSet<>();
    componentScan();
    dependencyInjection();
  }


  public void componentScan() throws Exception {

    Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Component.class);

    for (Class<?> component : annotated) {
      if (!component.isAnnotation()) {
        beans.add(component.getConstructor().newInstance());
        logger.debug("Component : {}", component);
      }
    }

    logger.debug("initiated beans : {}", beans);

  }


  public void dependencyInjection() throws IllegalAccessException {

    for (Object bean : beans) {

      List<Field> fields = Arrays.stream(bean.getClass().getDeclaredFields())
          .filter(field -> field.isAnnotationPresent(Autowired.class))
          .collect(Collectors.toUnmodifiableList());

      for (Field field : fields) {

        field.setAccessible(true);
        Object obj = findByType(field.getType());
        field.set(bean, obj);

        logger.debug("DI : {} <- {}", bean.getClass().getTypeName(), obj.getClass().getTypeName());

      }

    }

  }


  private Object findByType(Class<?> type) {
    return beans.stream()
        .filter(bean -> bean.getClass().getTypeName().equals(type.getTypeName()))
        .findAny()
        .orElseThrow(IllegalArgumentException::new);
  }


  public Set<Object> getBeans() {
    return Collections.unmodifiableSet(beans);
  }


  public StaticResourceHandler getStaticResourceHandler() {
    return staticResourceHandler;
  }


  public ServletHandler getServletHandler() {
    return servletHandler;
  }


  public Set<Object> findByAnnotation(Class<? extends Annotation> target) {

    String targetName = target.getTypeName();

    return beans.stream()
        .filter(bean -> Arrays.stream(bean.getClass().getAnnotations())
            .anyMatch(source -> {
              return source.annotationType()
                  .getTypeName()
                  .equals(targetName);
            }))
        .collect(Collectors.toUnmodifiableSet());
  }


}
