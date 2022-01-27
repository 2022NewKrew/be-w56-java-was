package webserver.handler.typeResolver;

import app.http.HttpRequest;

public class MapTypeResolver implements TypeResolver {
    private static final MapTypeResolver mapTypeResolver = new MapTypeResolver();

    public static MapTypeResolver getInstance() {
        return mapTypeResolver;
    }

    private MapTypeResolver() {
    }

    @Override
    public Object getType(HttpRequest request) {
        return request.getBody().getBody();
    }
}
