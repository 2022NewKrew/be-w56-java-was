package webserver.resolver;

import static http.HttpSession.JSESSIONID;

import http.HttpHeader;
import http.HttpRequest;
import http.HttpSession;
import java.util.Map;
import util.HttpRequestUtils;
import webserver.SessionStore;

public class SessionArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(Class<?> clazz, HttpRequest httpRequest) {
        return clazz.equals(HttpSession.class);
    }

    @Override
    public Object resolveArgument(Object instance, HttpRequest httpRequest) {
        Map<String, String> attributes = HttpRequestUtils.parseCookies(
            httpRequest.getHeader(HttpHeader.COOKIE));
        String value = attributes.get(JSESSIONID);
        HttpSession session = SessionStore.find(value).orElseGet(HttpSession::new);
        httpRequest.setSession(session);
        return session;
    }
}
