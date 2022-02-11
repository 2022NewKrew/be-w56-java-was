package webserver.handler.typeResolver;

import app.http.HttpRequest;
import app.http.HttpResponse;
import webserver.template.Model;

public class MapTypeResolver implements TypeResolver {
    private static final MapTypeResolver mapTypeResolver = new MapTypeResolver();

    public static MapTypeResolver getInstance() {
        return mapTypeResolver;
    }

    private MapTypeResolver() {
    }

    @Override
    public Object getType(HttpRequest request, HttpResponse httpResponse, Model model) {
        return request.getBody().getBody();
    }
}
