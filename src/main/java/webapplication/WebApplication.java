package webapplication;

import http.*;
import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webapplication.data.AttributeTypes;
import webapplication.dto.ModelAndView;
import webapplication.dto.ViewRenderingResult;
import webapplication.renderer.ViewRenderer;
import webapplication.routes.Router;
import webapplication.utils.HttpResponseUtils;

import java.lang.reflect.Method;

public class WebApplication {

    private static final Logger log = LoggerFactory.getLogger(WebApplication.class);

    public static HttpResponse doService(HttpRequest request) {

        Method method = Router.getMethod(request);
        ModelAndView modelAndView = executeMethod(method, request);

        if(modelAndView.isRedirect()) {
            String viewName = modelAndView.getViewName();
            String location = modelAndView.getViewName().substring(viewName.indexOf("redirect:") + "redirect:".length());
            return HttpResponseUtils.createRedirect(request.getHttpVersion(), location);
        }

        ViewRenderingResult viewRenderingResult = null;
        try {
            viewRenderingResult = ViewRenderer.render(modelAndView.getViewName(), modelAndView.getModel());
        } catch (Exception ex) {
            log.info("{} : {}", WebApplication.class, ex.getMessage());
            return HttpResponseUtils.createInternalError(request.getHttpVersion());
        }

        MultiValueMap<String, String> headers = new MultiValueMap<>();
        if(modelAndView.isCookies()) {
            Cookies cookies = (Cookies) modelAndView.getModel().getAttribute(AttributeTypes.COOKIES.getCode()).orElseGet(() -> new Cookies());
            headers.add(HttpHeaders.SET_COOKIE, cookies.toHeaderString());
        }

        return HttpResponseUtils.createOk(request.getHttpVersion(), HttpHeaders.of(headers), viewRenderingResult.getBody());
    }

    public static boolean canService(HttpRequest request) {
        return Router.canRoute(request);
    }

    private static ModelAndView executeMethod(Method method, HttpRequest request) {
        ModelAndView ret = null;
        try {
            ret = (ModelAndView) method.invoke(null, request);
        } catch (Exception ex) {
            log.info("{} : {}", ex.getMessage());
        }
        return ret;
    }

}
