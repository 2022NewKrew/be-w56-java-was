package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.context.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HttpSessionHandler {
    private static final Logger log = LoggerFactory.getLogger(HttpSessionHandler.class);

    private final Map<String, HttpSession> httpSessionMapByCookie;

    public HttpSessionHandler() {
        this.httpSessionMapByCookie = new HashMap<>();
    }

    public HttpSession issueHttpSession() {
        String cookie = createCookie();
        HttpSession httpSession = new HttpSession(cookie);
        httpSessionMapByCookie.put(cookie, httpSession);
        return httpSession;
    }

    public HttpSession getHttpSessionByCookies(String[] cookies) {
        for (String cookie : cookies) {
            if (httpSessionMapByCookie.containsKey(cookie)) return httpSessionMapByCookie.get(cookie);
        }
        log.debug("Invalid Cookie values!");
        return null;
    }

    private static String createCookie() {
        byte[] bytes = new byte[16];
        new Random().nextBytes(bytes);
        StringBuilder result = new StringBuilder();
        for (byte temp : bytes) {
            result.append(String.format("%02x", temp));
        }
        return result.toString();
    }

}
