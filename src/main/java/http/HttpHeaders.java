package http;

import lombok.Getter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Getter
public class HttpHeaders implements Iterable<Map.Entry<String, String>> {

    private final Map<String, String> headers = new HashMap<>();

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return headers.entrySet().iterator();
    }

    public void add(String name, String value) {
        headers.put(name, value);
    }
}
