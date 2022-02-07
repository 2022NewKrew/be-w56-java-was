package util;

import java.util.function.Function;

@FunctionalInterface
public interface CheckedFunction<T, R, E extends Exception> {

    R apply(T input) throws E;
}
