package webserver.controller.common;

import util.request.HttpRequest;
import util.response.FileType;
import util.response.HttpResponse;
import util.response.HttpStatus;
import util.response.ModelAndView;
import webserver.controller.Controller;

public class NotFoundController implements Controller {
    @Override
    public boolean supports(HttpRequest httpRequest) {
        return true;
    }

    @Override
    public HttpResponse doHandle(HttpRequest httpRequest){
        ModelAndView modelAndView = new ModelAndView("error", FileType.STRING);
        modelAndView.addAttribute("message", "Not Found Page");

        return HttpResponse.<String>builder()
                .status(HttpStatus.NOT_FOUND)
                .modelAndView(modelAndView)
                .build();
    }
}
