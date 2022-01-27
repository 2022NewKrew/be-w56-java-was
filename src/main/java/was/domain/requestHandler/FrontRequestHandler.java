package was.domain.requestHandler;

import di.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Bean
public class FrontRequestHandler implements RequestHandler {

    private final Logger logger = LoggerFactory.getLogger(FrontRequestHandler.class);
    private final List<RequestHandler> requestHandlerList = new LinkedList<>();

    public FrontRequestHandler(ControllerMappingHandler controllerMappingHandler,
                               StaticViewResolverHandler staticViewResolverHandler,
                               NegotiationHandler negotiationHandler) {
        requestHandlerList.add(controllerMappingHandler);
        requestHandlerList.add(staticViewResolverHandler);
        requestHandlerList.add(negotiationHandler);
    }

    @Override
    public void handle(HttpRequest req, HttpResponse res) throws IOException {
        long start = System.currentTimeMillis();
        logger.info("method:{}, path:{}, 요청 시작", req.getMethod(), req.getPath());

        for (RequestHandler requestHandler : requestHandlerList) {
            requestHandler.handle(req, res);
        }

        long end = System.currentTimeMillis();
        logger.info("method:{}, path:{}, 요청 완료. 소요 시간 {}s", req.getMethod(), req.getPath(), (end - start) / 1000.0);
    }
}
