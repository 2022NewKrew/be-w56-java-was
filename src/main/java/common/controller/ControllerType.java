package common.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum ControllerType {
    USER("users"),
    STATIC("webapp");

    private final String url;

    public static Stream<ControllerType> stream() {
        return Stream.of(ControllerType.values());
    }

    public static ControllerType of(String url) {
        return ControllerType.stream()
                .filter(controllerType -> controllerType.equals(url))
                .findFirst()
                .orElse(null);
    }

    public boolean equals(String url) {
        return Objects.equals(this.url, url);
    }
}
