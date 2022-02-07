package webserver;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ModelAndView {
    private final Map<String, List> attributes = new HashMap<String,List>();
    private String viewName;

    public void addAttribute(String key, List value){
        attributes.put(key, value);
    }

    public void setViewName(String viewName){
        this.viewName = viewName;
    }

    public List getAttribute(String key){
        return attributes.get(key);
    }
}
