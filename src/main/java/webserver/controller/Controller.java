package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.request.HttpRequest;
import util.response.HttpResponse;

public interface Controller<T> {
    Logger log = LoggerFactory.getLogger(Controller.class);

    default HttpResponse<?> handle(HttpRequest httpRequest){
        try{
            return doHandle(httpRequest);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return HttpResponse.of(e);
        }
    }

    boolean supports(HttpRequest httpRequest);
    HttpResponse<T> doHandle(HttpRequest httpRequest);
}
