package webserver.context;

import webserver.configures.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class Model implements ModelAndView {

    private final Map<String, Object> data;

    public Model() {
        this.data = new HashMap<>();
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void addAttribute(String name, Object value) {
        data.put(name, value);
    }

}
