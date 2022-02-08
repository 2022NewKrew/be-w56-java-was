package servlet.filter;

import servlet.ServletRequest;
import servlet.ServletResponse;
import servlet.container.Servlet;

import java.util.List;

public abstract class ServletFilter {
    private final List<String> urlPatterns;

    public ServletFilter(List<String> urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    public void process(ServletRequest request, ServletResponse response, Servlet servlet) {
        if (urlPatterns.contains(request.getPath())) {
            doFilter(request, response);
        }
        if (response.isEmpty()) {
            servlet.service(request, response);
        }
    }

    abstract void doFilter(ServletRequest request, ServletResponse response);
}
