package infrastructure.model;

import java.util.Arrays;
import java.util.Optional;

public enum RequestMethod {

    GET("GET"),
    POST("POST");

    private final String value;

    RequestMethod(String value) {
        this.value = value;
    }

    public static Optional<RequestMethod> getMethod(String value) {
        return Arrays.stream(RequestMethod.values())
                .filter(e -> value.equals(e.getValue()))
                .findAny();
    }

    public String getValue() {
        return value;
    }
}
