package servlet.filter;

import servlet.ServletRequest;
import servlet.ServletResponse;
import servlet.container.Servlet;

import java.util.List;

public abstract class ServletFilter {
    private final List<String> urlPatterns;

    public ServletFilter(List<String> urlPatterns) {
        this.urlPatterns = urlPatterns;
        initialize();
    }

    public ServletResponse process(ServletRequest request, Servlet servlet) {
        if (urlPatterns.contains(request.getPath())) {
            return doFilter(request, servlet);
        }
        return servlet.service(request);
    }

    abstract void initialize();

    abstract ServletResponse doFilter(ServletRequest request, Servlet servlet);
}
