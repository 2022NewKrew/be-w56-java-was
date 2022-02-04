package framework.controller;

import framework.util.Invalidator;
import framework.util.exception.InternalServerErrorException;
import framework.view.ModelView;
import framework.view.ViewResolver;
import framework.webserver.HttpRequestHandler;
import framework.webserver.HttpResponseHandler;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static framework.util.Constants.CONTEXT_PATH;
import static framework.util.Constants.DEFAULT_ERROR_PAGE;

/**
 * Front Contoller (Dispatcher Servlet),
 * Client로부터 받은 요청과 응답을 처리
 */
public class FrontController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class);
    private static final FrontController INSTANCE = new FrontController();

    public static FrontController getInstance() {
        return INSTANCE;
    }

    private FrontController() {
    }

    /**
     * Client로부터 받은 요청 정보를 처리하여 알맞은 응답 형식을 만들어 Client에게 응답해주는 메소드
     * @param request Client로부터 받은 요청 정보
     * @param response Client에게 응답해줄 정보
     */
    public void process(HttpRequestHandler request, HttpResponseHandler response) {
        try {
            // 먼저 Static file이라 가정하고 ModelView 생성
            String uri = request.getUri();
            ModelView modelView = ModelView.builder()
                    .isStatic(true)
                    .uri(uri)
                    .build();

            // Static file이 아니라면 Controller에게 전달
            if (!Invalidator.isStaticFile(CONTEXT_PATH + uri)) {
                HandlerMapper.handle(request, response, modelView);
            }

            // Session에 있는 모든 값을 ModelView에 저장
            modelView.addSessionAttributes(request.getSession());

            // View Resolver에게 전달하여 View를 만듦
            ViewResolver.resolve(modelView);
          
            // Client에게 응답
            response.flush(modelView);
        } catch (InternalServerErrorException e) {
            LOGGER.error(e.getMessage());

            ModelView modelView = ModelView.builder()
                    .isStatic(true)
                    .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .uri(DEFAULT_ERROR_PAGE)
                    .build();

            ViewResolver.resolve(modelView);
            response.flush(modelView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
