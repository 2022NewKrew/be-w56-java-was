package http.header;

import lombok.Getter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

@Getter
public class HttpHeaders implements Iterable<Map.Entry<String, String>> {

    private final Map<String, String> headers = new HashMap<>();

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return headers.entrySet().iterator();
    }

    public void add(String name, String value) {
        // 헤더 name은 case insensitive
        // https://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.2
        headers.put(name.toLowerCase(), value);
    }

    public void remove(String name) {
        headers.remove(name.toLowerCase());
    }

    public boolean containsName(String name) {
        return headers.containsKey(name.toLowerCase());
    }

    public String getValue(String name) {
        if (!containsName(name)) {
            throw new NoSuchElementException();
        }

        return headers.get(name.toLowerCase());
    }
}
