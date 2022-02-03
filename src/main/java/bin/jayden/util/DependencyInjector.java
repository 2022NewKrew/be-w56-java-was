package bin.jayden.util;

import bin.jayden.exception.CannotInjectDependencyException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependencyInjector {
    private static final Map<Class<?>, Object> beans = new HashMap<>();

    public static Object getControllerInstance(Class<?> controller) {
        try {
            Object instance = getInstance(controller);
            if (instance != null)
                return instance;
        } catch (Exception ignored) {
        }
        throw new CannotInjectDependencyException(controller.getName() + "의 의존성을 주입할 수 없습니다.");
    }

    private static Object getInstance(Class<?> cl) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (beans.containsKey(cl))
            return beans.get(cl);
        Constructor<?>[] constructors = cl.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameters = constructor.getParameterTypes();
            List<Object> parameterList = new ArrayList<>();
            for (Class<?> parameterClass : parameters) {
                Object instance = getInstance(parameterClass);
                if (instance == null)
                    break;
                parameterList.add(instance);
            }
            if (parameterList.size() == parameters.length) {
                Object instance = constructor.newInstance(parameterList.toArray());
                beans.put(cl, instance);
                return instance;
            }
        }
        return null;
    }
}
