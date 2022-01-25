package http;

import java.util.Objects;

public class FieldValue {
    private final String value;

    public FieldValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        FieldValue fieldValue = (FieldValue) object;
        return Objects.equals(value, fieldValue.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
