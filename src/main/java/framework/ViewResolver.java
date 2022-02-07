package framework;

import framework.modelAndView.ModelAndView;
import framework.modelAndView.View;
import util.HttpRequest;
import util.HttpResponse;
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

    public void render(ModelAndView mv, HttpRequest req, HttpResponse res) throws IOException {

        // 임시
        String viewName = mv.getViewName();
        int period = viewName.lastIndexOf(':');

        viewName = viewName.substring(period + 1);

        res.addHeader("Location", viewName);

        byte[] body = Files.readAllBytes(new File("./webapp" + viewName).toPath());
        res.addHeader("Content-Type", MIME.getMediaType(viewName));
        res.setBody(body);
    }

    public View resolveViewName(String viewName) {
//        if (!viewMap.containsKey(viewName))
//            return null;
//        return viewMap.get(viewName);

        for (String supportView : viewMap.keySet()) {
            if (viewName.startsWith(supportView)) {
                return viewMap.get(supportView);
            }
        }

        return null;
    }
}
