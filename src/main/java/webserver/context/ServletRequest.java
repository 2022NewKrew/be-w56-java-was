package webserver.context;

import webserver.configures.HttpServletRequest;
import webserver.configures.NativeWebRequest;

import java.util.Map;

public class ServletRequest implements HttpServletRequest, NativeWebRequest {

    private final Request request;
    private final Map<String,String> pathVar;
    private final HttpSession httpSession;
    private final Model model;

    public ServletRequest(Request request, Map<String, String> pathVar, HttpSession httpSession, Model model) {
        this.request = request;
        this.pathVar = pathVar;
        this.httpSession = httpSession;
        this.model = model;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public Model getModel() {
        return model;
    }

    public String getUrl() {
        return request.getUrl();
    }

    @Override
    public HttpSession getSession() {
        return getHttpSession();
    }

    @Override
    public void setAttribute(String key, long value) {
        model.addAttribute(key, value);
    }

    @Override
    public String getParameter(String name) {
        if (pathVar.containsKey(name))
            return pathVar.get(name);
        if (request.contains(name))
            return request.getParameter(name);
        return null;
    }

    @Override
    public Object getNativeRequest() {
        return this;
    }
}
