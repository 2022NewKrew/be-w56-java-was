package was.http.domain.service.requestHandler.requestDispatcher.methodArgumentResolver;

import was.http.domain.request.HttpRequest;
import was.http.domain.response.HttpResponse;

public interface MethodArgumentResolver {
    boolean supportsParameter(Class<?> clazz);

    Object resolveArgument(Class<?> clazz, HttpRequest httpRequest, HttpResponse httpResponse);
}
