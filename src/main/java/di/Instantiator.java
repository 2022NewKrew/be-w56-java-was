package di;

import java.lang.reflect.InvocationTargetException;

public interface Instantiator {

    Class<?>[] getParameterTypes();
    Object newInstance(Object[] parameters)
            throws InstantiationException, IllegalAccessException, InvocationTargetException;
}
