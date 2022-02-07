package webserver.handler.typeResolver;

import app.http.HttpRequest;
import app.http.HttpResponse;

public interface TypeResolver {
    Object getType(HttpRequest request, HttpResponse httpResponse);
}
