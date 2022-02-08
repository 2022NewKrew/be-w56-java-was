package lib.was.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassInstantiator implements Instantiator {

    private final Constructor<?> constructor;
    private Object instance = null;

    public ClassInstantiator(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return constructor.getParameterTypes();
    }

    @Override
    public Object newInstance(Object[] parameters)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        if (instance == null) {
            instance = constructor.newInstance(parameters);
        }
        return instance;
    }
}
