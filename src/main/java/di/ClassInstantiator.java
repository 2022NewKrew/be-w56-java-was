package di;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassInstantiator implements Instantiator {

    private final Constructor<?> constructor;

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
        return constructor.newInstance(parameters);
    }
}
