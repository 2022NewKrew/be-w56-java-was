package webserver.handler.typeResolver;

import app.http.HttpRequest;
import app.http.HttpResponse;

public class HttpResponseTypeResolver implements TypeResolver{
    private static final HttpResponseTypeResolver httpResponseTypeResolver = new HttpResponseTypeResolver();

    public static HttpResponseTypeResolver getInstance() {
        return httpResponseTypeResolver;
    }

    private HttpResponseTypeResolver() {
    }

    @Override
    public Object getType(HttpRequest request, HttpResponse response) {
        return response;
    }
}
