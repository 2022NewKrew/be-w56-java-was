package webserver.controller.common;

import util.request.HttpRequest;
import util.response.*;
import webserver.controller.Controller;

public class NotFoundController implements Controller {
    @Override
    public boolean supports(HttpRequest httpRequest) {
        return true;
    }

    @Override
    public HttpResponse doHandle(HttpRequest httpRequest){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addAttribute("message", "Not Found Page");

        return HttpResponse.builder(HttpStatus.NOT_FOUND, ContentType.PLANE_TEXT)
                .modelAndView(modelAndView)
                .build();
    }
}
