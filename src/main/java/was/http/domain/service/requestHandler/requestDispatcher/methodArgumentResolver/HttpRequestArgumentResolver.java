package was.http.domain.service.requestHandler.requestDispatcher.methodArgumentResolver;

import di.annotation.Bean;
import was.http.domain.request.HttpRequest;
import was.http.domain.response.HttpResponse;

@Bean
public class HttpRequestArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean supportsParameter(Class<?> clazz) {
        return clazz == HttpRequest.class;
    }

    @Override
    public Object resolveArgument(Class<?> clazz, HttpRequest httpRequest, HttpResponse httpResponse) {
        return httpRequest;
    }
}
