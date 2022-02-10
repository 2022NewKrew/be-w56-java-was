package webserver.handler;

import lombok.extern.slf4j.Slf4j;
import webserver.exception.ParamNotMatching;
import webserver.http.HttpRequest;

import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A configurable parser for {@link HttpRequest#getQueryStrings()}.
 * <p>
 * Configure at creation and use {@link #parse(HttpRequest)} to get parsed name to value map.
 * <p>
 * Only support 8 primitive types and their wrapper types.
 */
@Slf4j
public class HandlerParamParser {
    private final Map<String, Class<?>> paramNameToType;

    public HandlerParamParser(Map<String, Class<?>> paramNameToType) {
        this.paramNameToType = paramNameToType;
    }

    /**
     * Parse the given {@link HttpRequest#getQueryStrings()}.
     * <p>
     * Note that exactly all query strings should be matched.
     *
     * @param httpRequest
     * @return
     */
    public ParsedParams parse(HttpRequest httpRequest) {
        try {
            return parseWithException(httpRequest);
        } catch (ParamNotMatching e) {
            return new ParsedParams();
        }
    }

    private ParsedParams parseWithException(HttpRequest httpRequest) {
        if (httpRequest.getQueryStrings().size() != paramNameToType.size()) {
            throw new ParamNotMatching();
        }

        return new ParsedParams(paramNameToType.entrySet().stream()
                                               .collect(Collectors.toMap(Map.Entry::getKey,
                                                                         entry -> parse(
                                                                                 httpRequest.getQueryStrings().get(
                                                                                         entry.getKey()),
                                                                                 entry.getValue()))));
    }

    private Object parse(String value, Class<?> targetType) {
        if (targetType == String.class) {
            return value;
        }

        if (targetType.isPrimitive()) {
            targetType = MethodType.methodType(targetType).wrap().returnType();
        }
        try {
            return targetType.getDeclaredMethod("valueOf", String.class).invoke(null, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
            throw new ParamNotMatching();
        }
    }
}
