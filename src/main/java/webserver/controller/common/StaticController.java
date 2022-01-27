package webserver.controller.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.request.HttpRequest;
import util.request.MethodType;
import util.response.HttpResponse;
import util.response.HttpResponseDataType;
import util.response.HttpResponseStatus;
import webserver.controller.Controller;

import java.io.IOException;
import java.util.Set;

public class StaticController implements Controller<String> {
    private static final Logger log = LoggerFactory.getLogger(StaticController.class);
    private static final Set<String> supportExtensions = Set.of("html", "css", "js", "ico", "eot", "svg", "ttf", "woff", "woff2", "png");

    @Override
    public boolean supports(HttpRequest httpRequest){
        if(httpRequest.getMethod() != MethodType.GET){
            return false;
        }

        if(isRootUrl(httpRequest.getUrl())){
            return true;
        }

        String[] split = httpRequest.getUrl().split("\\.");
        String extensionName = split[split.length-1];
        return supportExtensions.contains(extensionName);
    }

    @Override
    public HttpResponse<String> handle(HttpRequest httpRequest) throws IOException {
        String fileName = isRootUrl(httpRequest.getUrl())
                ? "/index.html"
                : httpRequest.getUrl();

        log.info("return file {}", fileName);

        return HttpResponse.<String>builder()
                .status(HttpResponseStatus.SUCCESS)
                .data(fileName)
                .dataType(HttpResponseDataType.FILE_NAME)
                .build();
    }

    private boolean isRootUrl(String url){
        return url.equals("/") || url.equals("");
    }
}
