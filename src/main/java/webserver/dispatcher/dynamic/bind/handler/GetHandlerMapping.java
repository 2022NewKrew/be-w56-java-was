package webserver.dispatcher.dynamic.bind.handler;

import org.reflections.Reflections;
import webserver.dispatcher.dynamic.bind.annotation.GetMapping;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Singleton
 */
public class GetHandlerMapping extends MethodHandlerMapping {

    private static final GetHandlerMapping INSTANCE = new GetHandlerMapping();

    private GetHandlerMapping() {
    }

    public static GetHandlerMapping getInstance() {
        return INSTANCE;
    }

    @Override
    protected void setMappingTableWithReflections(Reflections reflections, Class<?> clazz) {
        Set<Method> methods = reflections.getMethodsAnnotatedWith(GetMapping.class);
        for(Method method : methods) {
            GetMapping annotation = method.getAnnotation(GetMapping.class);
            String[] uris = annotation.value();
            for(String uri : uris) {
                this.mappingTable.put(uri, new ClassAndMethod(clazz, method));
            }
        }
    }
}
