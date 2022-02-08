package handler.result;

import http.response.Cookie;
import http.response.Status;

import java.util.List;

public class View extends HandlerResult {
    private final Status status;
    private final String viewName;

    public View(List<Cookie> cookies, Status status, String viewName) {
        super(cookies);
        this.status = status;
        this.viewName = viewName;
    }

    public Status getStatus() {
        return status;
    }

    public String getViewName() {
        return viewName;
    }

    public static class Builder extends HandlerResult.Builder {
        protected Status status = Status.OK;
        protected final String viewName;

        protected Builder(String viewName) {
            super();
            this.viewName = viewName;
        }

        public static Builder of(String viewName) {
            return new Builder(viewName);
        }

        public Builder addCookie(Cookie cookie) {
            super.addCookie(cookie);
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public View build() {
            return new View(cookies, status, viewName);
        }
    }
}
