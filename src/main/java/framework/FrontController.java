package framework;

import framework.http.HttpRequest;
import framework.http.HttpResponse;
import framework.util.annotation.AnnotationProcessor;
import framework.view.View;


public class FrontController {
    private static final FrontController frontController = new FrontController();

    private FrontController() {
    }

    public static FrontController getInstance() {
        return frontController;
    }

    public void process(HttpRequest request, HttpResponse response) {
        try {
            ModelAndView mv = (ModelAndView) AnnotationProcessor.getInstance().requestMappingProcessor(request, response);

            if (mv == null) {
                mv = new ModelAndView(request.getUrl());
            }

            View view = ViewResolver.getInstance().resolve(mv);
            view.render(mv.getModel(), request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
