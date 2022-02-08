package servlet.container;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ParameterConstructor {
    private final Constructor<?> constructor;

    private ParameterConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public static ParameterConstructor create(Class<?> parameter, Fields fields) {
        return new ParameterConstructor(fields.createConstructor(parameter));
    }

    public Object makeInstance(Object[] fields) {
        // TODO 예외처리
        try {
            return constructor.newInstance(fields);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
