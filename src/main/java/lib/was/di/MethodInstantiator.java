package lib.was.di;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

class MethodInstantiator implements Instantiator {

    private final Method method;
    private final Supplier<Object> receiverSupplier;

    public MethodInstantiator(Method method, Supplier<Object> receiverSupplier) {
        this.method = method;
        this.receiverSupplier = receiverSupplier;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public Object newInstance(Object[] parameters)
            throws IllegalAccessException, InvocationTargetException {
        return method.invoke(receiverSupplier.get(), parameters);
    }
}
