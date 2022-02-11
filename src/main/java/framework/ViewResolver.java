package framework;

import framework.modelAndView.ModelAndView;
import framework.modelAndView.View;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import util.MIME;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class ViewResolver {

    private final Map<String, View> viewMap;

    public ViewResolver(Map<String, View> viewMap) {
        this.viewMap = viewMap;
    }

    public Map<String, View> getViewMap() {
        return viewMap;
    }

    // 정규표현식으로 반별
    public View resolveViewName(String viewName) {
        for (String supportView : viewMap.keySet()) {
            if (viewName.matches(supportView)) {
                return viewMap.get(supportView);
            }
        }

        return null;
    }
}
