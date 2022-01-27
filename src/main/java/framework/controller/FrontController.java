package framework.controller;

import framework.util.exception.InternalServerErrorException;
import framework.view.ModelView;
import framework.view.ViewResolver;
import framework.webserver.HttpRequestHandler;
import framework.webserver.HttpResponseHandler;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static framework.util.Constants.CONTEXT_PATH;
import static framework.util.Constants.DEFAULT_ERROR_PAGE;

/**
 * Front Contoller (Dispatcher Servlet),
 * Client로부터 받은 요청과 응답을 처리
 */
public class FrontController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class);
    private static FrontController instance;

    public static FrontController getInstance() {
        instance = new FrontController();
        return instance;
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
            // 먼저 static file이라 가정하고 ModelView 생성
            String uri = request.getUri();
            ModelView modelView = ModelView.builder()
                    .isStatic(true)
                    .uri(uri)
                    .build();

            // Static file이 아니라면 Controller에게 전달
            if (!isStatic(uri)) {
                modelView = HandlerMapper.handle(request, response);
            }

            // View Resolver에게 전달하여 View를 만듦
            ViewResolver.resolve(modelView);

            // Controller에게 응답
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

    /**
     * 경로를 받아 static file인지 확인해주는 메소드
     * @param path 확인할 경로
     * @return Static file 여부
     */
    private boolean isStatic(String path) {
        return new File(CONTEXT_PATH + path).exists();
    }
}
