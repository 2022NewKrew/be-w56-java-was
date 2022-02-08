package was.http.domain.service.requestHandler.requestDispatcher.methodArgumentResolver;

import di.annotation.Bean;
import was.http.domain.response.HttpResponse;
import was.http.domain.service.requestHandler.requestDispatcher.controllerMapper.MethodInvocation;
import was.http.domain.request.HttpRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Bean
public class MethodArgumentResolverChain {

    private final List<MethodArgumentResolver> methodArgumentResolverList = new ArrayList<>();

    public MethodArgumentResolverChain(HttpRequestArgumentResolver httpRequestArgumentResolver,
                                       HttpResponseArgumentResolver httpResponseArgumentResolver) {

        methodArgumentResolverList.add(httpRequestArgumentResolver);
        methodArgumentResolverList.add(httpResponseArgumentResolver);
    }

    public Object[] resolve(MethodInvocation methodInvocation, HttpRequest httpRequest, HttpResponse httpResponse) {
        return Arrays.stream(methodInvocation.getParameterTypes())
                .map(parameterType -> resolve(parameterType, httpRequest, httpResponse)).toArray();
    }

    private Object resolve(Class<?> parameterType, HttpRequest httpRequest, HttpResponse httpResponse) {
        return methodArgumentResolverList.stream()
                .filter(methodArgumentResolver -> methodArgumentResolver.supportsParameter(parameterType))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("해당하는 메소드 아규먼트 리졸버가 없습니다."))
                .resolveArgument(parameterType, httpRequest, httpResponse);
    }
}
