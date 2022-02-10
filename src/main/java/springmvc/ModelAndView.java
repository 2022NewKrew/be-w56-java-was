package springmvc;

import webserver.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private String view;
    private HttpStatus status;
    private Map<String, Object> model = new HashMap<>();

    public ModelAndView() {
    }

    public ModelAndView(String view, HttpStatus status) {
        this.view = view;
        this.status = status;
    }

    public ModelAndView(String view, HttpStatus status, Map<String, Object> model) {
        this.view = view;
        this.status = status;
        if (model != null) {
            this.model = model;
        }
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



}
