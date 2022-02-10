package springmvc;

import webserver.HttpStatus;

import java.util.Map;

public class ModelAndView {

    private String view;
    private Map<String, Object> model;
    private HttpStatus status;

    public ModelAndView() {

    }

    public ModelAndView(String view) {
        this.view = view;
    }

    public ModelAndView(String view, Map<String, Object> model) {
        this.view = view;
        if (model != null) {
            this.model = model;
        }
    }

    public ModelAndView(String view, HttpStatus status) {
        this.view = view;
        this.status = status;
    }

    public String getView() {
        return view;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ModelAndView(String view, HttpStatus status, Map<String, Object> model) {
        this.view = view;
        this.status = status;
        if (model != null) {
            this.model = model;
        }
    }

    public boolean isBody() {
        return !view.startsWith("/");
    }
}
