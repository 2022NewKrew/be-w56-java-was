package webserver.model;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    Map<String, String> model = new HashMap<>();
    String viewName;

    public ModelAndView() {
    }

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public void setModel(Map<String, String> model) {
        this.model = model;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, String> getModel() {
        return model;
    }

    public String getViewName() {
        return viewName;
    }
}
