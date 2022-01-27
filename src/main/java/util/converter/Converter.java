package util.converter;

import java.lang.reflect.InvocationTargetException;

public interface Converter<T, R>{
    boolean support(Class<?> fromType, Class<?> toType);
    R convert(T from) throws InvocationTargetException, InstantiationException, IllegalAccessException;
}
