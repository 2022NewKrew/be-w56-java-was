package view;

import model.ModelAndView;
import view.redirect.ViewRedirect;
import view.render.ViewDynamic;
import view.render.ViewStatic;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static util.ConstantValues.REDIRECT_COMMAND;
import static util.ConstantValues.REDIRECT_IDX;


public class ViewResolver {
    private final Map<String, String> viewMap = new ConcurrentHashMap<>();

    private static final ViewResolver viewResolver = new ViewResolver();

    private ViewResolver(){
        viewMap.put("/", "/index.html");
        viewMap.put("/users", "/user/list.html");
        viewMap.put("/users/form", "/user/form.html");
        viewMap.put("/users/login", "/user/login.html");
    }

    public static ViewResolver getInstance(){
        return viewResolver;
    }

    public View getView(ModelAndView mv) throws IOException {

        if(viewMap.get(mv.getViewName()) != null){
            mv.setViewName(viewMap.get(mv.getViewName()));
            return new ViewDynamic(mv);
        }

        return (mv.getViewName().indexOf(REDIRECT_COMMAND) == REDIRECT_IDX) ?
                new ViewRedirect(mv) : new ViewStatic(mv.getViewName());
    }
}
