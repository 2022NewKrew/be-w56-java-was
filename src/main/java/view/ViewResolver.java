package view;

import model.ModelAndView;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ViewResolver {
    private final Map<String, String> viewMap = new ConcurrentHashMap<>();

    private static final ViewResolver viewResolver = new ViewResolver();

    private ViewResolver(){
        viewMap.put("/", "/index.html");
        viewMap.put("/users/form", "/user/form.html");
    }

    public static ViewResolver getInstance(){
        return viewResolver;
    }

    public ModelAndView getView(ModelAndView mv){
        if(viewMap.get(mv.getViewName()) != null){
            mv.setViewName(viewMap.get(mv.getViewName()));
        }
        return mv;
    }
}
