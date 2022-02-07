package webserver.controller.common;

import util.request.HttpRequest;
import util.response.HttpResponse;
import util.response.HttpResponseDataType;
import util.response.HttpResponseStatus;
import util.response.ModelAndView;
import webserver.controller.Controller;

public class NotFoundController implements Controller<String> {
    @Override
    public boolean supports(HttpRequest httpRequest) {
        return true;
    }

    @Override
    public HttpResponse<String> doHandle(HttpRequest httpRequest){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addAttribute("message", "Not Found Page");

        return HttpResponse.<String>builder()
                .status(HttpResponseStatus.NOT_FOUND)
                .modelAndView(modelAndView)
                .build();
    }
}
