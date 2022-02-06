package handler;

import com.google.common.collect.Lists;
import http.response.Cookie;

import java.util.List;

public class HandlerResult {
    private final String uri;
    private final boolean redirect;
    private final List<Cookie> cookies;

    public HandlerResult(String uri, boolean redirect, List<Cookie> cookies) {
        this.uri = uri;
        this.redirect = redirect;
        this.cookies = cookies;
    }

    public boolean isRedirect() {
        return redirect;
    }

    public String getUri() {
        return uri;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public static class Builder {
        private String uri;
        private boolean redirect;
        private List<Cookie> cookies;

        private Builder(boolean redirect) {
            this.redirect = redirect;
            cookies = Lists.newArrayList();
        }

        public static Builder of() {
            return new Builder(false);
        }

        public static Builder ofRedirect() {
            return new Builder(true);
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder addCookie(Cookie cookie) {
            cookies.add(cookie);
            return this;
        }

        public HandlerResult build() {
            return new HandlerResult(uri, redirect, cookies);
        }
    }
}
