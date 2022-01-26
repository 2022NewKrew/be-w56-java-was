package was.domain.requestHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import was.domain.controller.Controller;
import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;
import was.meta.UrlPath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FrontRequestHandler implements RequestHandler {

    private final Logger logger = LoggerFactory.getLogger(FrontRequestHandler.class);

    private final List<RequestHandler> requestHandlerList = new ArrayList<>();

    private FrontRequestHandler() {
    }

    public static FrontRequestHandler getInstance(Map<UrlPath, Controller> requestHandlers) {
        final FrontRequestHandler instance = FrontRequestHandlerWrapper.INSTANCE;

        instance.requestHandlerList.add(ControllerMappingHandler.getInstance(requestHandlers));
        instance.requestHandlerList.add(NegotiationHandler.getInstance());

        return instance;
    }

    private static class FrontRequestHandlerWrapper {
        private static final FrontRequestHandler INSTANCE = new FrontRequestHandler();
    }

    @Override
    public void handle(HttpRequest req, HttpResponse res) throws IOException {
        long start = System.currentTimeMillis();
        logger.info("요청 시작");

        for (RequestHandler requestHandler : requestHandlerList) {
            requestHandler.handle(req, res);
        }

        long end = System.currentTimeMillis();
        logger.info("요청 완료. 소요 시간 {}s", (end - start) / 1000.0);
    }
}
