package was.domain.http;

import di.annotation.Bean;
import was.domain.eventLoop.EventService;
import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;
import was.domain.requestHandler.requestHandlerChain.RequestHandlerChain;
import was.domain.requestHandler.requestHandlerChain.RequestHandlerChainImpl;
import was.util.HttpMapper;

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
