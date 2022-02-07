package webserver.handler;

import lombok.extern.slf4j.Slf4j;
import webserver.http.HttpRequest;

import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Slf4j
public class HandlerMatcher {
    private final String path;
    private final Map<String, Class<?>> requiredParams;

    public HandlerMatcher(String path, Map<String, Class<?>> requiredParams) {
        this.path = path;
        this.requiredParams = requiredParams;
    }

    /**
     * Check query string type and path matching
     *
     * @param httpRequest
     * @return
     */
    public boolean match(HttpRequest httpRequest) {
        return httpRequest.getPath().equals(path)
                && httpRequest.getQueryStrings().size() == requiredParams.size()
                && (httpRequest.getQueryStrings().isEmpty() || httpRequest.getQueryStrings().entrySet().stream().anyMatch(
                entry -> isStringValueOfType(entry.getValue(),
                                             requiredParams.getOrDefault(entry.getKey(), NoSuchElement.class))));
    }

    private boolean isStringValueOfType(String value, Class<?> targetTypeToParseAs) {
        // https://stackoverflow.com/questions/18097720/java-how-to-check-if-a-string-value-is-a-type-of-given-class
        // Assume "valueOf" is defined on such Type, which is what WrapperType do.
        // So we need to first cast primitive types to their wrapper types.
        if (targetTypeToParseAs.isPrimitive()) {
            targetTypeToParseAs = MethodType.methodType(targetTypeToParseAs).wrap().returnType();
        }
        try {
            targetTypeToParseAs.getDeclaredMethod("valueOf", String.class).invoke(null, value);
            return true;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * A default class for getting non-existing class from requiredParams.
     * <p>
     * Since this a custom class no String value will ever match this class.
     */
    private static class NoSuchElement {
    }
}
