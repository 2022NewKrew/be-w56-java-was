package webserver.servlet;

import java.io.File;
import java.io.IOException;
import template.Model;
import template.View;
import webserver.WebServerConfig;

public class ViewResolver {

    private ViewResolver() {
    }

    static ViewResolver getInstance() {
        return ViewResolverHolder.INSTANCE;
    }

    public View handle(String path, Model model) throws IOException {
        return new View(new File(WebServerConfig.BASE_PATH + path), model);
    }

    private static class ViewResolverHolder {

        public static ViewResolver INSTANCE = new ViewResolver();
    }

}
