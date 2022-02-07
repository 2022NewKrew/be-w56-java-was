package webserver.handler;

import webserver.configures.WebMvcConfigurer;
import webserver.core.ComponentManager;

public class WebConfigHandler {

    public static void init(ComponentManager componentManager, ArgumentResolverHandler argumentResolverHandler, InterceptorHandler interceptorHandler) {
        WebMvcConfigurer webMvcConfigurer = (WebMvcConfigurer) componentManager.getObjectByClazz(WebMvcConfigurer.class);
        webMvcConfigurer.addArgumentResolvers(argumentResolverHandler.getArgumentResolvers());
        webMvcConfigurer.addInterceptors(interceptorHandler.issueInterceptorRegistry());
    }

}
