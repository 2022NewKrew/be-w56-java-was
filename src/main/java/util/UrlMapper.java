package util;

import util.annotation.Controller;
import util.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UrlMapper {
    private final Map<String, Method> getUrlMethodMap;
    private final Map<Method, Object> methodClassMap;

    public UrlMapper() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        getUrlMethodMap = new HashMap<>();
        methodClassMap = new HashMap<>();
        GetMapping getMapping = null;
        AccessingAllClassesInPackage aac = new AccessingAllClassesInPackage();
        Set<Class> s = aac.findAllClassesUsingClassLoader("controller");
        Iterator<Class> it = s.iterator();
        while(it.hasNext()){
            Class cls = Class.forName(it.next().getName());
            if(cls.getAnnotation(Controller.class) == null)
                continue;
            Method[] methods = cls.getMethods();
            for(Method method : methods) {
                methodClassMap.put(method, cls.getConstructor().newInstance());
                if ((getMapping = method.getAnnotation(GetMapping.class)) != null) {
                    getUrlMethodMap.put(getMapping.url(), method);
                }
            }
        }

    }

    public Object control(HttpRequest httpRequest) throws InvocationTargetException, IllegalAccessException {
        Method method = this.getUrlMethodMap.get(httpRequest.url());
        Object controller = this.methodClassMap.get(method);

        if(method == null)
            return httpRequest.url();

        if(httpRequest.params() == null)
            return method.invoke(controller);
        return method.invoke(controller, httpRequest.params());
    }


    public static class AccessingAllClassesInPackage {

        public Set<Class> findAllClassesUsingClassLoader(String packageName) {
            InputStream stream = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream(packageName.replaceAll("[.]", "/"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            return reader.lines()
                    .filter(line -> line.endsWith(".class"))
                    .map(line -> getClass(line, packageName))
                    .collect(Collectors.toSet());
        }

        private Class getClass(String className, String packageName) {
            try {
                return Class.forName(packageName + "."
                        + className.substring(0, className.lastIndexOf('.')));
            } catch (ClassNotFoundException e) {
                // handle the exception
            }
            return null;
        }
    }

}
