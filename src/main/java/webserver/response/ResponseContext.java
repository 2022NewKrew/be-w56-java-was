package webserver.response;

/**
 * Singleton
 */
public class ResponseContext {

    private static final ResponseContext INSTANCE = new ResponseContext();

    public static ResponseContext getInstance() {
        return INSTANCE;
    }

    private ResponseContext() {
    }

    private ThreadLocal<HttpResponse> httpResponseHolder = new ThreadLocal<>();

    public HttpResponse createResponse() {
        HttpResponse httpResponse = createEmptyResponse();
        httpResponseHolder.set(httpResponse);
        return httpResponse;
    }

    private HttpResponse createEmptyResponse() {
        return new HttpResponse(new HttpResponseLine(), new HttpResponseHeader(), new HttpResponseBody());
    }

    public HttpResponse getHttpResponse() {
        return httpResponseHolder.get();
    }

    public void endResponse() {
        httpResponseHolder.remove();
    }
}
