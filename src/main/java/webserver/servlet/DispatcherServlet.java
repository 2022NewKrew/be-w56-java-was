package webserver.servlet;

import com.google.common.net.HttpHeaders;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import template.Model;
import template.View;
import webserver.WebServerConfig;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpResponseStatus;
import webserver.http.MimeSubtype;
import webserver.http.MimeType;
import webserver.util.FileUtils;

public class DispatcherServlet {

    private final HandlerMapping handlerMapping;
    private final HandlerAdapter handlerAdapter;
    private final ViewResolver viewResolver;
    private final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private DispatcherServlet() {
        this.handlerMapping = HandlerMapping.getInstance();
        this.handlerAdapter = HandlerAdapter.getInstance();
        this.viewResolver = ViewResolver.getInstance();
    }

    public static DispatcherServlet getInstance() {
        return HttpHandlerHolder.INSTANCE;
    }

    public void handle(HttpRequest request, HttpResponse response) {
        logger.info("{} {}", request.getMethod(), request.getUri());
        Model model = new Model();
        try {
            String viewUri = getViewUri(handlerAdapter.handle(request, response, model));

            if (!request.getUri().equals(viewUri)) {
                setRedirect(response, viewUri);
                return;
            }

            View view = viewResolver.handle(viewUri, model);
            setViewContents(response, view);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.setStatus(HttpResponseStatus.INTERNAL_ERROR);
            response.setBody(e.getMessage().getBytes(StandardCharsets.UTF_8));
        }
    }

    private String getViewUri(String path) {
        if (FileUtils.hasExtension(path)) {
            return path;
        }
        return path + WebServerConfig.ENTRY_EXTENSION;
    }

    private void setViewContents(HttpResponse response, View view) {
        String extension = FileUtils.parseExtension(view.getFile());

        response.setStatus(HttpResponseStatus.OK);
        response.headers()
            .set(HttpHeaders.CONTENT_TYPE, MimeType.getMimeSubtype(extension));
        
        if (view.getContents().length() > 0) {
            response.setBody(view.getContents().getBytes(StandardCharsets.UTF_8));
        }
    }

    private void setRedirect(HttpResponse response, String path) {
        logger.info("[redirect] " + WebServerConfig.ENDPOINT + path);
        response.setStatus(HttpResponseStatus.FOUND);
        response.headers()
            .set(HttpHeaders.CONTENT_TYPE, MimeSubtype.TEXT_HTML)
            .set(HttpHeaders.LOCATION, WebServerConfig.ENDPOINT + path);
    }

    private static class HttpHandlerHolder {

        private static final DispatcherServlet INSTANCE = new DispatcherServlet();
    }

}
