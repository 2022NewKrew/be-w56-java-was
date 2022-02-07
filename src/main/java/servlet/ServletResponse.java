package servlet;

import http.Cookie;

public class ServletResponse {
    private Object response;
    private Model model;
    private Cookie cookie;

    public ServletResponse(Object response, Model model, Cookie cookie) {
        this.response = response;
        this.model = model;
        this.cookie = cookie;
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
}
