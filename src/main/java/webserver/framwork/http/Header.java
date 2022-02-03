package webserver.framwork.http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Header implements Iterable<Header.Pair> {

    private final List<Pair> headers = new ArrayList<>();

    public void addValue(String key, String value) {
        headers.add(new Pair(key, value));
    }

    public String getValue(String key) {
        return headers.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(NoSuchElementException::new)
                .getValue();
    }

    @Override
    public Iterator<Pair> iterator() {
        return headers.iterator();
    }

    public static class Pair {
        String key;
        String value;

        public Pair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
