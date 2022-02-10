package webserver.handler.typeResolver;

import app.http.HttpRequest;
import app.http.HttpResponse;
import webserver.template.Model;

public interface TypeResolver {
    Object getType(HttpRequest request, HttpResponse httpResponse, Model model);
}
