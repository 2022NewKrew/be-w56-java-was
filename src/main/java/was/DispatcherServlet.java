package was;

import was.controller.Controller;
import was.controller.StaticResourceController;
import was.http.HttpMapper;
import was.http.HttpRequest;
import was.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import was.meta.UrlPath;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class DispatcherServlet {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final Map<UrlPath, Controller> handlers;

    public DispatcherServlet(Map<UrlPath, Controller> handlers) {
        this.handlers = handlers;
    }

    public byte[] doService(String rawRequest) {
        HttpRequest request = Objects.requireNonNull(HttpMapper.toHttpRequest(rawRequest));
        HttpResponse response = Objects.requireNonNull(HttpMapper.fromHttpRequest(request));
//        log.info(request.toString());

        try {
            doDispatch(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        log.info(response.toString());
        return response.toBytes();
    }

    private void doDispatch(HttpRequest request, HttpResponse response) {
        Controller controller = getHandler(request);

        try {
            controller.handle(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Controller getHandler(HttpRequest request) {
        final UrlPath urlPath = UrlPath.findUrlPathByHttpRequest(request);
        final Controller handler = handlers.get(urlPath);

        if (Objects.isNull(handler)) {
            return StaticResourceController.getInstance();
        }

        return handler;
    }
}
