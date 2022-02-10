package webserver.handler.typeResolver;

import app.http.HttpRequest;
import app.http.HttpResponse;
import webserver.template.Model;

public class ModelTypeResolver implements TypeResolver{
    private static final ModelTypeResolver modelTypeResolver = new ModelTypeResolver();

    public static ModelTypeResolver getInstance() {
        return modelTypeResolver;
    }

    private ModelTypeResolver() {
    }

    @Override
    public Object getType(HttpRequest request, HttpResponse httpResponse, Model model) {
        return model;
    }
}
