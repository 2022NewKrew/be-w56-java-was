package http;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Headers {
    private final Map<FieldName, FieldValue> headers;

    public Headers(Map<FieldName, FieldValue> headers) {
        this.headers = headers;
    }

    public static Headers create(Map<String, String> map) {
        return new Headers(map.entrySet()
                .stream()
                .map(header -> Header.create(header.getKey(), header.getValue()))
                .collect(Collectors.toMap(Header::getFieldName, Header::getFieldValue)));
    }

    public static Headers create(List<String> headers) {
        Map<FieldName, FieldValue> map = headers.stream()
                .map(Header::create)
                .collect(Collectors.toMap(Header::getFieldName, Header::getFieldValue));
        return new Headers(map);
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
        boolean isEqualValues = this.headers.entrySet()
                .stream()
                .allMatch(header -> header.getValue().equals(headers.headers.get(header.getKey())));

        return isEqualKeySet && isEqualValues;
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers);
    }

    public Map<FieldName, FieldValue> getHeaders() {
        return headers;
    }

    public Optional<String> sendContainKeyValue(String key) {
        FieldName name = new FieldName(key);
        if (headers.containsKey(name)) {
            return Optional.of(headers.get(name).getValue());
        }
        return Optional.empty();
    }
}
