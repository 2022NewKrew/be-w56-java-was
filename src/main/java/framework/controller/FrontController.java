package framework.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import framework.view.ModelView;
import framework.view.ViewResolver;
import framework.webserver.HttpRequestHandler;
import framework.webserver.HttpResponseHandler;

import java.io.File;

import static framework.util.Constants.CONTEXT_PATH;

public class FrontController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class);
    private static FrontController instance;

    public static FrontController getInstance() {
        instance = new FrontController();
        return instance;
    }

    private FrontController() {
    }

    public void process(HttpRequestHandler request, HttpResponseHandler response) {
        try {
            String uri = request.getUri();
            ModelView modelView = ModelView.builder()
                    .isStatic(true)
                    .uri(uri)
                    .build();

            if (!isStatic(uri)) {
                modelView = HandlerMapper.handle(request, response);
            }

            ViewResolver.resolve(modelView);
            response.flush(modelView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isStatic(String path) {
        return new File(CONTEXT_PATH + path).exists();
    }
}
