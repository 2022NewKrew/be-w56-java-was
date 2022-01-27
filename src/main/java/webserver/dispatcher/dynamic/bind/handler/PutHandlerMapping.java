package webserver.dispatcher.dynamic.bind.handler;

import org.reflections.Reflections;
import webserver.dispatcher.dynamic.bind.annotation.PutMapping;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Singleton
 */
public class PutHandlerMapping extends MethodHandlerMapping {

    private static final PutHandlerMapping INSTANCE = new PutHandlerMapping();

    private PutHandlerMapping() {
    }

    public static PutHandlerMapping getInstance() {
        return INSTANCE;
    }

    @Override
    protected void setMappingTableWithReflections(Reflections reflections, Class<?> clazz) {
        Set<Method> methods = reflections.getMethodsAnnotatedWith(PutMapping.class);
        for(Method method : methods) {
            PutMapping annotation = method.getAnnotation(PutMapping.class);
            String[] uris = annotation.value();
            for(String uri : uris) {
                this.mappingTable.put(uri, new ClassAndMethod(clazz, method));
            }
        }
    }
}
