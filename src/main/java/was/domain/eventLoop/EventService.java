package was.domain.eventLoop;

import di.annotation.Bean;
import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;
import was.domain.requestHandler.FrontRequestHandler;
import was.domain.requestHandler.RequestHandler;
import was.meta.HttpStatus;
import was.util.HttpMapper;

import java.io.IOException;
import java.util.function.Consumer;

@Bean
public class EventService {
    private final RequestHandler frontRequestHandler;
    private final HttpMapper httpMapper;

    public EventService(FrontRequestHandler frontRequestHandler, HttpMapper httpMapper) {
        this.frontRequestHandler = frontRequestHandler;
        this.httpMapper = httpMapper;
    }

    public byte[] doService(byte[] requestBytes) {
        final HttpRequest req = httpMapper.toHttpRequest(requestBytes);
        final HttpResponse res = httpMapper.toHttpResponse(req);

        try {
            frontRequestHandler.handle(req, res);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();

            res.setStatus(HttpStatus.FORBIDDEN);
            res.initHeaders();
            res.setBody(new byte[]{});
        }

        return res.toHttp();
    }
}
