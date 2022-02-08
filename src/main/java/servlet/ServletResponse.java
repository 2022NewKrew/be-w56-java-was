package servlet;

import http.header.Cookie;
import servlet.view.Model;

public class ServletResponse {
    private Object response;
    private Model model;
    private Cookie cookie;

    public ServletResponse(Object response, Model model, Cookie cookie) {
        this.response = response;
        this.model = model;
        this.cookie = cookie;
    }

    public ServletResponse(Object response, Cookie cookie) {
        this(response, new Model(), cookie);
    }

    public ServletResponse(Object response) {
        this(response, new Model(), null);
    }

    public ServletResponse() {
        this(null, new Model(), null);
    }

    public Model getModel() {
        return model;
    }

    public boolean isRedirect() {
        return response.getClass() == String.class && ((String) response).contains("redirect:");
    }

    public boolean isStatic() {
        return response.getClass() == String.class && ((String) response).contains(".") && model.isEmpthy();
    }

    public boolean isModelView() {
        return true;
    }

    public String getPath() {
        return (String) response;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public boolean isEmpty() {
        return response == null;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
