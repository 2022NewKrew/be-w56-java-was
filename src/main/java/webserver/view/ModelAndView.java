package webserver.view;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ModelAndView {
    private final Map<String, Object> attributes = new HashMap<>();
    private String viewName;

    public void addAttribute(String key, Object value){
        attributes.put(key, value);
    }

    public void setViewName(String viewName){
        this.viewName = viewName;
    }

    public Object getAttribute(String key){
        return attributes.get(key);
    }
}
