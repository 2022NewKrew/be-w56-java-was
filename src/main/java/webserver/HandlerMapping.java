package webserver;

import webserver.http.HttpRequest;
import webserver.annotations.GetMapping;
import webserver.annotations.PostMapping;
import webserver.http.enums.HttpMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class HandlerMapping {
    private final Map<String, Pair> postMethods;
    private final Map<String, Pair> getMethods;

    public HandlerMapping(Map<String, Object> beans) {
        postMethods = registerMethods(beans, PostMapping.class);
        getMethods = registerMethods(beans, GetMapping.class);
    }

    private Map<String, Pair> registerMethods(Map<String, Object> beans, Class<? extends Annotation> annotation) {
        Map<String, Pair> methods = new HashMap<>();

        for (Object object : beans.values()) {
            methods.putAll(Arrays.stream(object.getClass().getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(annotation))
                    .collect(HashMap::new,
                            (map, m) -> {
                                Annotation annotation_ = m.getAnnotation(annotation);
                                String[] urls = new String[]{};

                                if (annotation_ instanceof PostMapping)
                                    urls = ((PostMapping)m.getAnnotation(annotation)).value();
                                else if (annotation_ instanceof GetMapping)
                                    urls = ((GetMapping)m.getAnnotation(annotation)).value();

                                for (String url: urls)
                                    map.put(url, new Pair(object, m));
                            },
                            HashMap::putAll));
        }

        return methods;
    }

    public Pair findMethod(HttpRequest httpRequest) {
        String uri = httpRequest.getUri();

        if (httpRequest.getMethod() == HttpMethod.POST)
            return postMethods.get(uri);
        return getMethods.get(uri);
    }

    public static class Pair {
        Object object;
        Method method;

        Pair(Object object, Method method) {
            this.object = object;
            this.method = method;
        }

        public Object getObject() {
            return object;
        }

        public Method getMethod() {
            return method;
        }
    }
}
