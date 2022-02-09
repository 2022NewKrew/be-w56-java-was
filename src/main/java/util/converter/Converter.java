package util.converter;

public interface Converter<T, R>{
    boolean support(Class<?> fromType, Class<?> toType);
    R convert(T from);
}
