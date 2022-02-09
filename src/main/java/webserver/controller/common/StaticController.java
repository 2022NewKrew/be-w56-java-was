package webserver.controller.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.request.HttpRequest;
import util.request.MethodType;
import util.response.*;
import webserver.controller.Controller;

public class StaticController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(StaticController.class);

    @Override
    public boolean supports(HttpRequest httpRequest){
        if(httpRequest.getMethod() != MethodType.GET){
            return false;
        }

        if(isRootUrl(httpRequest.getUrl())){
            return true;
        }

        return ContentType.getFileType(httpRequest.getUrl()).isPresent();
    }

    @Override
    public HttpResponse doHandle(HttpRequest httpRequest){
        String fileName = isRootUrl(httpRequest.getUrl())
                ? "/index.html"
                : httpRequest.getUrl();

        log.info("return file {}", fileName);

        ContentType contentType = ContentType.getFileType(fileName).orElseThrow();
        return HttpResponse.builder(HttpStatus.SUCCESS, contentType)
                .modelAndView(new ModelAndView(fileName))
                .build();
    }

    private boolean isRootUrl(String url){
        return url.equals("/") || url.equals("");
    }
}
