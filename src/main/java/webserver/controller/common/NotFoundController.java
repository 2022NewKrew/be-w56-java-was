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

        ResponseHeaders responseHeaders = ResponseHeaders.builder()
                .contentType(ContentType.PLANE_TEXT)
                .build();

        return HttpResponse.<String>builder()
                .status(HttpStatus.NOT_FOUND)
                .modelAndView(modelAndView)
                .headers(responseHeaders)
                .build();
    }
}
