package webserver;

import webserver.enums.HttpStatus;
import webserver.model.ModelAndView;
import webserver.model.http.HttpResponse;

public class ViewResolver {
    public static HttpResponse resolve(HttpResponse httpResponse, ModelAndView modelAndView) {
        String viewName = modelAndView.getViewName();

        if (viewName.contains("redirect:")) {
            httpResponse.redirect(viewName.substring("redirect:".length()))
                    .setStatusLine(HttpStatus.FOUND);
            return httpResponse;
        }

        httpResponse.setView(modelAndView.getViewName())
                .setStatusLine(HttpStatus.OK);

        return httpResponse;
    }
}
