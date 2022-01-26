package view;

import model.ModelAndView;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static util.ConstantValues.*;

public class ViewResolver {
    private final Map<String, String> viewMap = new ConcurrentHashMap<>();

    private static final ViewResolver viewResolver = new ViewResolver();

    private ViewResolver(){
        viewMap.put("/", "/index.html");
    }

    public static ViewResolver getInstance(){
        return viewResolver;
    }

    public String getView(ModelAndView mv){
        return viewMap.get(mv.getViewName()) != null ?
                viewMap.get(mv.getViewName()) : mv.getViewName();
    }
}
