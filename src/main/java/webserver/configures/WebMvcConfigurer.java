package webserver.configures;

import java.util.List;

public interface WebMvcConfigurer {

    default void addInterceptors(InterceptorRegistry registry) {
    }

    default void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    }

}
