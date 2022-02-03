package webserver.framwork.core;

import webserver.framwork.core.view.InternalResourceView;
import webserver.framwork.core.view.RedirectView;
import webserver.framwork.core.view.View;

import java.io.File;


public class ViewResolver {
    public static final String VIEW_PREFIX = "./webapp/";
    public static final String VIEW_POSTFIX = ".html";
    public static ViewResolver instance = new ViewResolver();

    private ViewResolver() {
    }

    public static ViewResolver getInstance() {
        return instance;
    }

    public View resolve(String viewName, Model model) {

        int redirectLocationStartIdx = viewName.indexOf(":");
        if (redirectLocationStartIdx != -1) {
            String redirectUrl = viewName.substring(redirectLocationStartIdx + 1);
            return new RedirectView(redirectUrl);
        }

        String viewFileLocation = VIEW_PREFIX + viewName + VIEW_POSTFIX;
        if (new File(viewFileLocation).exists()) {
            return new InternalResourceView(viewFileLocation, model);
        }

        return null;
    }
}
