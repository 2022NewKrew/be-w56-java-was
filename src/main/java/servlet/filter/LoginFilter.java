package servlet.filter;

import http.header.Cookie;
import servlet.ServletRequest;
import servlet.ServletResponse;

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
    public void doFilter(ServletRequest request, ServletResponse response) {
        Cookie cookie = request.getCookie("logined");
        if (cookie.getValue().equals("false")) {
            response.setPath("redirect:/user/login.html");
        }
    }
}
