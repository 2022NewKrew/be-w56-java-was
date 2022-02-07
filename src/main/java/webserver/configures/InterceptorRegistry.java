package webserver.configures;

import java.util.List;

public interface InterceptorRegistry {

    default InterceptorRegistry addInterceptor(HandlerInterceptor interceptor) {
        return null;
    }

    default InterceptorRegistry addPathPatterns(List<String> patterns) {
        return null;
    }

    default InterceptorRegistry excludePathPatterns() {
        return null;
    }

}
