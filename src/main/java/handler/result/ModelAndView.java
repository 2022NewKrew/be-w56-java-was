package handler.result;

import com.google.common.collect.Maps;
import http.response.Cookie;
import http.response.Status;

import java.util.List;
import java.util.Map;

public class ModelAndView extends View {
    private final Map<String, Object> models;

    private ModelAndView(List<Cookie> cookies, Status status, String viewName, Map<String, Object> models) {
        super(cookies, status, viewName);
        this.models = models;
    }

    public Map<String, Object> getModels() {
        return models;
    }

    public static class Builder extends View.Builder {
        private final Map<String, Object> models;

        private Builder(String viewName) {
            super(viewName);
            this.models = Maps.newHashMap();
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

        public Builder addModel(String key, Object value) {
            models.put(key, value);
            return this;
        }

        public ModelAndView build() {
            return new ModelAndView(cookies, status, viewName, models);
        }
    }
}
