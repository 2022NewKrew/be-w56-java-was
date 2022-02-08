package filter;

import com.google.common.collect.Sets;
import http.request.RawHttpRequest;

import java.util.Set;

public class FilterChain {
    private static FilterChain instance;
    private Set<Filter> filters;

    private FilterChain() {
        init();
    }

    public synchronized static FilterChain getInstance() {
        if (instance == null) {
            instance = new FilterChain();
        }
        return instance;
    }

    private void init() {
        filters = Sets.newHashSet();

        filters.add(new ContentTypeFilter());
    }

    public void doFilter(RawHttpRequest request) {
        filters.forEach(filter -> filter.doFilter(request));
    }
}
