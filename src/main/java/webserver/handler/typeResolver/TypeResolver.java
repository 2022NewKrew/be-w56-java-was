package webserver.handler.typeResolver;

import app.http.HttpRequest;

public interface TypeResolver {
    Object getType(HttpRequest request);
}
