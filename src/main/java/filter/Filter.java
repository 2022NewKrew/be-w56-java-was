package filter;

import http.request.RawHttpRequest;

public interface Filter {

    void doFilter(RawHttpRequest request);
}
