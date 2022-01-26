package controller;

import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mapping 되어 있지 않는 요청을 처리하는 controller
 */
public class DefaultController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(DefaultController.class);

    @Override
    public void makeResponse(HttpRequest request, HttpResponse response) {

        String url = request.getUrl();

        try {
            // url 에서 요청하는 static file 이 있으면 응답으로 file 을 준다.
            response.makeStaticFileResponse(url);
        } catch (Exception e) {
            // url 에서 요청하는 file 도 없으면 오류 페이지를 준다.
            // TODO: 오류페이지 만들기
            log.error(e.getMessage());
        }
    }
}
