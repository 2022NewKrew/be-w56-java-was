package webserver.dispatcher.dynamic.bind.handler;

import org.reflections.Reflections;
import webserver.dispatcher.dynamic.bind.annotation.DeleteMapping;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Singleton
 */
public class DeleteHandlerMapping extends MethodHandlerMapping {

    private static final DeleteHandlerMapping INSTANCE = new DeleteHandlerMapping();

    private DeleteHandlerMapping() {
    }

    public static DeleteHandlerMapping getInstance() {
        return INSTANCE;
    }

    @Override
    protected void setMappingTableWithReflections(Reflections reflections, Class<?> clazz) {
        Set<Method> methods = reflections.getMethodsAnnotatedWith(DeleteMapping.class);
        for(Method method : methods) {
            DeleteMapping annotation = method.getAnnotation(DeleteMapping.class);
            String[] uris = annotation.value();
            for(String uri : uris) {
                this.mappingTable.put(uri, new ClassAndMethod(clazz, method));
            }
        }
    }
}
