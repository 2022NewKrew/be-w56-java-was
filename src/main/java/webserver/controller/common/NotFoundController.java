package webserver.controller.common;

import util.request.HttpRequest;
import util.response.HttpResponse;
import util.response.HttpResponseDataType;
import util.response.HttpResponseStatus;
import webserver.controller.Controller;

public class NotFoundController implements Controller<String> {
    @Override
    public boolean supports(HttpRequest httpRequest) {
        return true;
    }

    @Override
    public HttpResponse<String> handle(HttpRequest httpRequest){
        return HttpResponse.<String>builder()
                .status(HttpResponseStatus.NOT_FOUND)
                .data("Not Found")
                .dataType(HttpResponseDataType.STRING)
                .build();
    }
}
