package templates;

import java.util.*;

public class TemplateAttribute<T> {
    private final Map<String, Collection<T>> map = new HashMap<>();

    public void set(final String key, final T obj) {
        map.put(key, List.of(obj));
    }

    public void set(final String key, final Collection<T> col) {
        map.put(key, col);
    }

    public Collection<T> get(final String key) {
        return Collections.unmodifiableCollection(
                map.getOrDefault(key, Collections.emptyList())
        );
    }
}
