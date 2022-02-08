package lib.was.db;

@FunctionalInterface
public interface CheckedFunction<T, R, E extends Exception> {

    R apply(T input) throws E;
}
