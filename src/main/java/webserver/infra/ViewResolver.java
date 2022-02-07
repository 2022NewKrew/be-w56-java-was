package webserver.infra;

import webserver.model.ModelAndView;
import webserver.view.ForwardView;
import webserver.view.RedirectView;
import webserver.view.View;

public class ViewResolver {
    private static final String DEFAULT_PREFIX = "./webapp";

    public ViewResolver() {}

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
