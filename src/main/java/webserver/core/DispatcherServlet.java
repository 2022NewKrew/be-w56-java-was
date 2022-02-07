package webserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Dispatcher;
import webserver.context.*;
import webserver.handler.HttpSessionHandler;
import webserver.handler.InterceptorHandler;
import webserver.handler.MethodHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

public class DispatcherServlet implements Dispatcher {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final InterceptorHandler interceptorHandler;

    private final HandlerMapping handlerMapping;

    private final HttpSessionHandler httpSessionHandler;

    public DispatcherServlet(InterceptorHandler interceptorHandler, HandlerMapping handlerMapping) {
        this.interceptorHandler = interceptorHandler;
        this.handlerMapping = handlerMapping;
        this.httpSessionHandler = new HttpSessionHandler();
    }

    private HttpSession getOrCreateHttpSessionBy(Request request) {
        HttpSession httpSession = httpSessionHandler.getHttpSessionByCookies(request.getCookie().replace(" ", "").split(";"));
        if (httpSession == null) httpSession = httpSessionHandler.issueHttpSession();
        return httpSession;
    }

    private String checkHiddenHtttpMethod(Request request) {
        if (request.contains("_method")) return request.getParameter("_method").toUpperCase();
        return request.getMethod();
    }

    public void dispatch(Request request, OutputStream out) {
        try {
            if (ViewResolver.resolveStaticView(out, request.getUrl())) return;
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpSession httpSession = getOrCreateHttpSessionBy(request);
        Model model = new Model();

        Url requestUrl = Url.of(Url.HttpMethod.valueOf(checkHiddenHtttpMethod(request)), request.getUrl(), false);
        ServletRequest servletRequest = new ServletRequest(request, handlerMapping.getUrlPatternBy(requestUrl).getValidatedPathVar(requestUrl), httpSession, model);
        ServletResponse servletResponse = new ServletResponse(new Response(""), httpSession, model);
        MethodHandler methodHandler = handlerMapping.getMethodHandlerBy(requestUrl);

        try {
            if (interceptorHandler.preHandle(servletRequest, servletResponse, methodHandler)) {
                Object responseData = methodHandler.invokeMethodBy(servletRequest);
                servletResponse.setResponse(new Response(responseData));
            }

            interceptorHandler.postHandle(servletRequest, servletResponse, methodHandler, model);

            ViewResolver.resolveModelAndView(out, servletResponse);

            interceptorHandler.afterCompletion(servletRequest, servletResponse, methodHandler);

        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
