package webserver;

import http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private HttpStatus status = HttpStatus.OK;
    private String viewName;
    private Model model;
    private final Map<String, String> cookies = new HashMap<>();

    public ModelAndView() {}

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public ModelAndView(String viewName, Model model) {
        this.viewName = viewName;
        this.model = model;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void addCookie(String key, String value) {
        cookies.put(key, value);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getViewName() {
        return viewName;
    }

    public Model getModel() {
        return model;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }
}
