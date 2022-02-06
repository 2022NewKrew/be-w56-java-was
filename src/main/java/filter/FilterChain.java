package filter;

import com.google.common.collect.Sets;
import http.request.RawHttpRequest;

import java.util.Set;

public class FilterChain {
    private Set<Filter> filters;

    public FilterChain() {
        init();
    }

    private void init() {
        filters = Sets.newHashSet();

        filters.add(new ContentTypeFilter());
    }

    public void doFilter(RawHttpRequest request) {
        filters.forEach(filter -> filter.doFilter(request));
    }
}
