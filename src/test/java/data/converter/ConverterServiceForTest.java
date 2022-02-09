package data.converter;

import org.junit.jupiter.params.provider.Arguments;
import util.converter.Converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ConverterServiceForTest {
    private static final Converter<Object, Map<String, String>> objectToFieldMapConverter = new ObjectToFieldMapConverter();

    public static List<Map<String, String>> convertToFieldMapList(List<?> from){
        return from.stream()
                .map(objectToFieldMapConverter::convert)
                .collect(toList());

    }

    public static Stream<Arguments> convertToArgumentsStream(List<?> from){
        return from.stream().map(Arguments::of);
    }
}
