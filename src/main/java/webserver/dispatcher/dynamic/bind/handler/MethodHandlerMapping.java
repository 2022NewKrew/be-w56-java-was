package webserver.dispatcher.dynamic.bind.handler;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import webserver.exception.RequestMethodNotFoundException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class MethodHandlerMapping {

    protected Map<String, ClassAndMethod> mappingTable = new HashMap<>();

    public void initMappingTable(Set<Class<?>> controllers) {
        for(Class<?> clazz : controllers) {
            Reflections reflections = new Reflections(clazz, Scanners.MethodsAnnotated);
            setMappingTableWithReflections(reflections, clazz);
        }
    }

    protected abstract void setMappingTableWithReflections(Reflections reflections, Class<?> clazz);

    public ClassAndMethod getMethod(String url) {
        Optional<ClassAndMethod> method = Optional.of(mappingTable.get(url));
        return method.orElseThrow(() -> new RequestMethodNotFoundException());
    }
}
