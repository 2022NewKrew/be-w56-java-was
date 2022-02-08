package http.startline;

import java.util.Arrays;
import java.util.Optional;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    NONE("NONE");

    private final String value;

    HttpMethod(String value) {
        this.value = value;
    }

    public static HttpMethod matchValue(String value) {
        Optional<HttpMethod> findMethod = Arrays.stream(values())
                .filter(httpMethod -> httpMethod.value.equals(value))
                .findAny();

        if (findMethod.isEmpty()) {
            return HttpMethod.NONE;
        }
        return findMethod.get();
    }
}
