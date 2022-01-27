package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Singleton
 */
public class RequestContext {

    private static final RequestContext INSTANCE = new RequestContext();

    private RequestContext() {
    }

    public static RequestContext getInstance() {
        return INSTANCE;
    }

    private ThreadLocal<HttpRequest> httpRequestHolder = new ThreadLocal<>();

    public HttpRequest startRequest(BufferedReader inputReader) throws IOException {
        HttpRequest httpRequest = createHttpRequest(inputReader);
        httpRequestHolder.set(httpRequest);
        return httpRequest;
    }

    private HttpRequest createHttpRequest(BufferedReader inputReader) throws IOException {
        return HttpRequestFactory.getInstance().createBy(inputReader);
    }

    public HttpRequest getHttpRequest() {
        return httpRequestHolder.get();
    }

    public void endRequest() {
        httpRequestHolder.remove();
    }
}
