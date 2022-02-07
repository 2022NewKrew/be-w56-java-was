package framework.modelAndView.view;

import framework.modelAndView.ModelAndView;
import framework.modelAndView.View;
import util.HttpRequest;
import util.HttpResponse;

import java.io.IOException;

public class RedirectView extends View {

    @Override
    public void render(ModelAndView mv, HttpRequest req, HttpResponse res) throws IOException {
        // 임시
        String viewName = mv.getViewName();
        int period = viewName.lastIndexOf(':');

        viewName = viewName.substring(period + 1);

        res.setStatusCode(302);
        res.addHeader("Location", viewName);

    }

}
