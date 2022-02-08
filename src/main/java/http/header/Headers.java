package http.header;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Headers {
    private final Map<FieldName, FieldValue> headers;

    private Headers(Map<FieldName, FieldValue> headers) {
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

    public Cookies createCookies() {
        Optional<String> cookie = sendContainKeyValue("Cookie");
        if (cookie.isEmpty()) {
            return Cookies.parse(null);
        }
        return Cookies.parse(cookie.get());
    }
}
