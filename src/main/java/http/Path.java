package http;

import com.google.common.base.Strings;

import java.util.Objects;

public class Path {
    private static final int START_INDEX = 0;
    private static final Character PREFIX = '/';
    private final String value;

    public Path(String value) {
        validateNullOrEmpty(value);
        validateStartWithPrefix(value);
        this.value = value;
    }

    private void validateNullOrEmpty(String value) {
        if (Strings.isNullOrEmpty(value)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateStartWithPrefix(String value) {
        if (value.charAt(START_INDEX) != PREFIX) {
            throw new IllegalArgumentException();
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Path path = (Path) object;
        return Objects.equals(value, path.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
