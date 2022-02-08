package was.http.domain.service;

import di.annotation.Bean;
import was.http.domain.request.HttpRequest;
import was.http.domain.response.HttpResponse;
import was.http.domain.service.requestHandler.requestHandlerChain.RequestHandlerChain;
import was.http.domain.service.requestHandler.requestHandlerChain.RequestHandlerChainImpl;
import was.server.domain.eventService.EventService;
import was.server.util.HttpMapper;

@Bean
public class HttpService implements EventService {
    private final RequestHandlerChain requestHandlerChain;
    private final HttpMapper httpMapper;

    public HttpService(RequestHandlerChainImpl requestHandlerChain, HttpMapper httpMapper) {
        this.requestHandlerChain = requestHandlerChain;
        this.httpMapper = httpMapper;
    }

    @Override
    public byte[] doService(byte[] requestBytes) {
        final HttpRequest req = httpMapper.toHttpRequest(requestBytes);
        final HttpResponse res = httpMapper.toHttpResponse(req);

        final RequestHandlerChain requestHandlerChainCopy = ((RequestHandlerChainImpl) requestHandlerChain).clone();

        requestHandlerChainCopy.handle(req, res);

        return res.toHttp();
    }
}
