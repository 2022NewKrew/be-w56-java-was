package servlet.filter;

import http.Cookie;
import servlet.ServletRequest;
import servlet.ServletResponse;
import servlet.container.Servlet;

import java.util.List;

public class LoginFilter extends ServletFilter {
    private List<String> urlPatterns;

    private LoginFilter(List<String> urlPatterns) {
        super(urlPatterns);
    }

    public static ServletFilter create(List<String> urlPatterns) {
        return new LoginFilter(urlPatterns);
    }

    @Override
    void initialize() {

    }

    @Override
    public ServletResponse doFilter(ServletRequest request, Servlet servlet) {
        Cookie cookie = request.getCookie();
        boolean logined = Boolean.getBoolean(cookie.getCookie("logined"));
        if (!logined) {
            return new ServletResponse("redirect:/user/login.html", cookie);
        }
        return servlet.service(request);
    }
}
