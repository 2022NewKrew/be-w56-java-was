package util.converter;

import java.util.List;

public class ConverterService {
    private static final List<Converter<?,?>> converters = List.of(
            new MapToUserConverter(),
            new MapToPostConverter()
    );

    public static <T, R> R convert(T from, Class<R> toType) {
        try {
            Converter<T, R> converter = (Converter<T, R>) converters.stream()
                    .filter(c -> c.support(from.getClass(), toType))
                    .findFirst()
                    .orElseThrow();

            return converter.convert(from);
        }catch (Exception e){
            throw new IllegalStateException("cannot convert");
        }
    }
}
