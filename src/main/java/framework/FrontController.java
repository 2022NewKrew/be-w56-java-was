package framework;

import framework.handler.Handler;
import framework.modelAndView.ModelAndView;
import framework.modelAndView.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;

public class FrontController {

    public static Logger log = LoggerFactory.getLogger(FrontController.class);

    private final HandlerMapping handlerMapping;
    private final ViewResolver viewResolver;

    // HandlerMapping 과 viewResolver를 주입
    public FrontController(HandlerMapping handlerMapping, ViewResolver viewResolver) {
        this.handlerMapping = handlerMapping;
        this.viewResolver = viewResolver;
    }

    public void doDispatch(HttpRequest req, HttpResponse res) throws IOException {

        //HandlerMapping(Handler 모음)에서 matching 된 Handler를 반환
        Handler mappedHandler = handlerMapping.getHandler(req);

        // Handler 가 없을 경우
        if (mappedHandler == null) {
            log.error("지원하는 Handler 가 없습니다. : " + req.getPath());
            res.setStatusCode(404);
            return ;
        }

        ModelAndView mv = null;

        // 반환할 html 이름을 반환 (Resource 일 경우 파일을 로드하고 null 반환)
        mv = mappedHandler.handle(req, res);
        // html 을 HttpResponse 의 body 로 로드
        if (mv != null)
            render(mv, req, res);

    }

    private void render(ModelAndView mv, HttpRequest req, HttpResponse res) throws IOException {
        View view = viewResolver.resolveViewName(mv.getViewName());
        if (view != null) {
            view.render(mv, req, res);
        }

    }
}
