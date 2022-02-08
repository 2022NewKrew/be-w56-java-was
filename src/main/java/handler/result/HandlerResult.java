package handler.result;

import com.google.common.collect.Lists;
import http.response.Cookie;

import java.util.List;

public class HandlerResult {
    private final List<Cookie> cookies;

    protected HandlerResult(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    protected static class Builder {
        protected List<Cookie> cookies;

        protected Builder() {
            cookies = Lists.newArrayList();
        }

        protected Builder addCookie(Cookie cookie) {
            cookies.add(cookie);
            return this;
        }
    }
}
