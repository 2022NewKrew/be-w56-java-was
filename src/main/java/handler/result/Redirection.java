package handler.result;

import http.response.Cookie;

import java.util.List;

public class Redirection extends HandlerResult {
    private final String uri;

    public Redirection(List<Cookie> cookies, String uri) {
        super(cookies);
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public static class Builder extends HandlerResult.Builder {
        private final String uri;

        private Builder(String uri) {
            super();
            this.uri = uri;
        }

        public static Builder of(String uri) {
            return new Builder(uri);
        }

        public Builder addCookie(Cookie cookie) {
            super.addCookie(cookie);
            return this;
        }

        public HandlerResult build() {
            return new Redirection(cookies, uri);
        }
    }
}
