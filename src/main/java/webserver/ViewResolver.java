package webserver;

import webserver.enums.HttpStatus;
import webserver.model.ModelAndView;
import webserver.model.http.HttpResponse;

public class ViewResolver {
    private static final String REDIRECT_PREFIX = "redirect:";

    public static HttpResponse resolve(HttpResponse httpResponse, ModelAndView modelAndView) {
        String viewName = modelAndView.getViewName();

        if (viewName.contains(REDIRECT_PREFIX)) {
            httpResponse.redirect(viewName.substring(REDIRECT_PREFIX.length()))
                    .setStatusLine(HttpStatus.FOUND);
            return httpResponse;
        }

        httpResponse.setBody(modelAndView)
                .setStatusLine(HttpStatus.OK);

        return httpResponse;
    }
}
