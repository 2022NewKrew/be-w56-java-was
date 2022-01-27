package model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModelAndView {

    private String viewName;
    private Map<String, Object> values;

    public ModelAndView(String viewName){
        this.viewName = viewName;
        this.values = new ConcurrentHashMap<>();
    }

    public ModelAndView(String viewName, Map<String, Object> values){
        this.viewName = viewName;
        this.values = values;
    }

    public ModelAndView(String viewName, String name, Object value){
        this.viewName = viewName;
        this.values = new ConcurrentHashMap<>();
        this.values.put(name, value);
    }

    public void setViewName(String view){
        this.viewName = view;
    }

    public void addObject(String name, Object value){
        this.values.put(name, value);
    }

    public void addAllObject(Map<String, Object> values){
        this.values = values;
    }

    public String getViewName(){
        return viewName;
    }

    public Object getValue(String name){ return values.get(name); }

    public Map<String, Object> getValues(){
        return values;
    }
}
