package controller;

import http.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.HttpStatus;
import java.io.IOException;

public abstract class AbstractController implements Controller {

    @Override
    public HttpResponse service(HttpRequest request) throws IOException {
        HttpMethod method = request.getMethod();

        if (method.equals(HttpMethod.GET)) {
            return doGet(request);
        }
        if (method.equals(HttpMethod.POST)) {
            return doPost(request);
        }
        return HttpResponse.error(HttpStatus.METHOD_NOT_ALLOWED);
    }

    protected abstract HttpResponse doGet(HttpRequest request) throws IOException;

    protected abstract HttpResponse doPost(HttpRequest request);

    public abstract boolean match(String path);
}
