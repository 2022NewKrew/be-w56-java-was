package webserver.dispatcher.dynamic.bind.handler;

import org.reflections.Reflections;
import webserver.dispatcher.dynamic.bind.annotation.PostMapping;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Singleton
 */
public class PostHandlerMapping extends MethodHandlerMapping {

    private static final PostHandlerMapping INSTANCE = new PostHandlerMapping();

    private PostHandlerMapping() {
    }

    public static PostHandlerMapping getInstance() {
        return INSTANCE;
    }

    @Override
    protected void setMappingTableWithReflections(Reflections reflections, Class<?> clazz) {
        Set<Method> methods = reflections.getMethodsAnnotatedWith(PostMapping.class);
        for(Method method : methods) {
            PostMapping annotation = method.getAnnotation(PostMapping.class);
            String[] uris = annotation.value();
            for(String uri : uris) {
                this.mappingTable.put(uri, new ClassAndMethod(clazz, method));
            }
        }
    }
}
