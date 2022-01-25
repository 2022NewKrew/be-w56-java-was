package http;

import java.util.Map;
import java.util.Objects;

public class Headers {
    private final Map<FieldName, FieldValue> headers;

    public Headers(Map<FieldName, FieldValue> headers) {
        this.headers = headers;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Headers headers = (Headers) object;

        boolean isEqualKeySet = Objects.equals(this.headers.keySet(), headers.headers.keySet());
        if(!isEqualKeySet) {
            return false;
        }

        boolean isEqualValue = this.headers.entrySet()
                .stream()
                .allMatch(header -> header.getValue().equals(headers.headers.get(header.getKey())));

        return isEqualValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers);
    }
}
