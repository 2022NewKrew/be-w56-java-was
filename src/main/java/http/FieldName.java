package http;

import java.util.Objects;

public class FieldName {
    private final String name;

    public FieldName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        FieldName fieldName = (FieldName) object;
        return Objects.equals(name, fieldName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }
}
