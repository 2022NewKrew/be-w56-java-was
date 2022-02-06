package http.request;

import java.util.Objects;

public class Uri {

    private final String value;

    Uri(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Uri uri = (Uri) o;
        return Objects.equals(value, uri.value);
    }

    public boolean equals(String s) {
        if (value == null) return s == null;
        return value.equals(s);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Uri{" +
                "value='" + value + '\'' +
                '}';
    }
}
