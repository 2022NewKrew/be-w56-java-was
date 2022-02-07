package webserver.view;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private String view;
    private final Map<String, Object> model;

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

    public Map<String, Object> getModel(){
        return model;
    }

    public void addAttribute(String key, Object value) {
        model.put(key, value);
    }
}
