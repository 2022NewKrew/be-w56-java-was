package webserver.processor;

import http.HttpHandler;
import http.HttpRequest;
import http.HttpResponse;

import java.util.List;

import static com.google.common.base.Preconditions.*;

public class HttpProcessor {

    private final List<HttpHandler> handlers;

    public HttpProcessor(List<HttpHandler> handlers) {
        this.handlers = handlers;
    }

    public HttpResponse process(HttpRequest httpRequest){
        HttpResponse httpResponse = null;
        try {
            HttpHandler handler = findHandler(httpRequest);
            checkNotNull(handler, "요청을 처리할 수 있는 적절한 Handler 가 없습니다.");
            httpResponse = handler.handle(httpRequest);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return httpResponse;
    }

    private HttpHandler findHandler(HttpRequest httpRequest) {
        for(HttpHandler handler : handlers) {
            if(handler.supports(httpRequest)) {
                return handler;
            }
        }
        return null;
    }
}
