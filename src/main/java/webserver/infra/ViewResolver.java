package webserver.infra;

import webserver.model.ModelAndView;
import webserver.view.ForwardView;
import webserver.view.RedirectView;
import webserver.view.View;

public class ViewResolver {
    private static final String DEFAULT_PREFIX = "./webapp";

    private static final ViewResolver instance = new ViewResolver();

    private ViewResolver() {}

    public static ViewResolver getInstance() {
        if (instance == null) {
            return new ViewResolver();
        }
        return instance;
    }

    public View resolve(ModelAndView modelAndView) {
        if (modelAndView.isRedirectView()) {
            return new RedirectView(modelAndView.getViewPath());
        }
        return new ForwardView(getFullViewPath(modelAndView.getViewPath()), modelAndView.getHttpStatus());
    }

    private String getFullViewPath(String viewPath) {
        return DEFAULT_PREFIX + viewPath;
    }

}
