package was.domain.event;

import was.domain.controller.Controller;
import was.domain.requestHandler.FrontRequestHandler;
import was.domain.requestHandler.RequestHandler;
import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;
import was.meta.HttpStatus;
import was.meta.UrlPath;
import was.util.HttpMapper;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

public class EventService {
    private RequestHandler frontRequestHandler;
    private final HttpMapper httpMapper = new HttpMapper();

    private EventService() {
    }

    public static EventService getInstance(Map<UrlPath, Controller> controllers) {
        final EventService instance = FrontServiceWrapper.INSTANCE;
        instance.frontRequestHandler = FrontRequestHandler.getInstance(controllers);
        return instance;
    }

    private static class FrontServiceWrapper {
        private static final EventService INSTANCE = new EventService();
    }

    public void doService(byte[] requestBytes, Consumer<byte[]> returnResponse) {
        final HttpRequest req = httpMapper.toHttpRequest(requestBytes);
        final HttpResponse res = httpMapper.toHttpResponse(req);

        try {
            frontRequestHandler.handle(req, res);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();

            res.setStatus(HttpStatus.FORBIDDEN);
            res.initHeaders();
            res.setBody(new byte[]{});
        } finally {
            returnResponse.accept(res.toHttp());
        }
    }
}
