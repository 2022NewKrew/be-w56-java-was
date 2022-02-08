package framework;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private final Map<String, Object> model;
    private String view;

    public ModelAndView(String view) {
        this.view = view;
        model = new HashMap<>();
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void addAttribute(String key, Object value) {
        model.put(key, value);
    }
}
