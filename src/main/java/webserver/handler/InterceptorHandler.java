package webserver.handler;

import webserver.configures.HandlerInterceptor;
import webserver.configures.InterceptorRegistry;
import webserver.configures.ModelAndView;
import webserver.context.ServletRequest;
import webserver.context.ServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class InterceptorHandler {

    private static InterceptorHandler interceptorHandler;

    private List<InterceptorRegistry> interceptorRegistrys;

    private InterceptorHandler(List<InterceptorRegistry> interceptorRegistrys) {
        this.interceptorRegistrys = interceptorRegistrys;
    }

    public static InterceptorHandler getInstance() {
        if (interceptorHandler == null) {
            interceptorHandler = new InterceptorHandler(new ArrayList<>());
        }
        return interceptorHandler;
    }

    public InterceptorRegistry issueInterceptorRegistry() {
        InterceptorRegistry interceptorRegistry = new InterceptorRegistryImpl();
        interceptorRegistrys.add(interceptorRegistry);
        return interceptorRegistry;
    }

    public boolean preHandle(ServletRequest servletRequest, ServletResponse servletResponse, Object handle) throws Exception {
        boolean flag = true;
        for (InterceptorRegistry interceptorRegistry : interceptorRegistrys) {
            InterceptorRegistryImpl interceptor = (InterceptorRegistryImpl)interceptorRegistry;
            if (interceptor.validateUrl(servletRequest.getUrl()))
                flag &= interceptor.getHandlerInterceptor().preHandle(servletRequest, servletResponse, handle);
        }
        return flag;
    }

    public void postHandle(ServletRequest servletRequest, ServletResponse servletResponse, Object handle, ModelAndView model) throws Exception {
        for (InterceptorRegistry interceptorRegistry : interceptorRegistrys) {
            InterceptorRegistryImpl interceptor = (InterceptorRegistryImpl)interceptorRegistry;
            if (interceptor.validateUrl(servletRequest.getUrl()))
                interceptor.getHandlerInterceptor().postHandle(servletRequest, servletResponse, handle, model);
        }
    }

    public void afterCompletion(ServletRequest servletRequest, ServletResponse servletResponse, Object handle) throws Exception {
        for (InterceptorRegistry interceptorRegistry : interceptorRegistrys) {
            InterceptorRegistryImpl interceptor = (InterceptorRegistryImpl)interceptorRegistry;
            if (interceptor.validateUrl(servletRequest.getUrl()))
                interceptor.getHandlerInterceptor().afterCompletion(servletRequest, servletResponse, handle);
        }
    }


    public class InterceptorRegistryImpl implements InterceptorRegistry {

        private HandlerInterceptor handlerInterceptor;

        private List<String> pathPatterns = new ArrayList<>();

        private List<String> excludePathPattern = new ArrayList<>();

        public InterceptorRegistryImpl() {
            this.pathPatterns = new ArrayList<>();
            this.excludePathPattern = new ArrayList<>();
        }

        public InterceptorRegistry addInterceptor(HandlerInterceptor interceptor) {
            this.handlerInterceptor = interceptor;
            return this;
        }

        public InterceptorRegistry addPathPatterns(List<String> patterns) {
            this.pathPatterns.addAll(patterns);
            return this;
        }

        public InterceptorRegistry excludePathPatterns() {
            return this;
        }

        public HandlerInterceptor getHandlerInterceptor() {
            return handlerInterceptor;
        }

        public boolean validateUrl(String url) {
            for (String pattern : pathPatterns) {
                if (Pattern.matches(pattern.replace("**", ".*"), url)) return true;
            }
            for (String pattern : excludePathPattern) {
                if (Pattern.matches(pattern.replace("**", ".*"), url)) return false;
            }
            return false;
        }

    }

}
