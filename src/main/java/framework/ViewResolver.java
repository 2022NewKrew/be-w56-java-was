package framework;

import framework.view.InternalResourceView;
import framework.view.RedirectView;
import framework.view.TemplateEngineView;
import framework.view.View;

import java.util.Arrays;

public class ViewResolver {
    private static final ViewResolver instance = new ViewResolver();
    private static final String[] RESOURCE_FILE_TYPE = {"/css", "/fonts", "/images", "/js"};
    private static final String REDIRECT_TAG = "redirect:";

    private ViewResolver() {
    }

    public static ViewResolver getInstance() {
        return instance;
    }

    public View resolve(ModelAndView mv) {
        String view = mv.getView();

        if (view.contains(REDIRECT_TAG)) {
            String redirectView = view.replace(REDIRECT_TAG, "");
            return new RedirectView(redirectView);
        }

        boolean isMatched = Arrays.stream(RESOURCE_FILE_TYPE).anyMatch(view::contains);
        if(isMatched){
            return new InternalResourceView(view);
        }

        return new TemplateEngineView(view);
    }
}
