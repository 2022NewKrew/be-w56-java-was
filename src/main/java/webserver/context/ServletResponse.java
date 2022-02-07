package webserver.context;

import webserver.configures.HttpServletResponse;

public class ServletResponse implements HttpServletResponse {

    private Response response;
    private final HttpSession httpSession;
    private final Model model;

    public ServletResponse(Response response, HttpSession httpSession, Model model) {
        this.response = response;
        this.httpSession = httpSession;
        this.model = model;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public Model getModel() {
        return model;
    }

    public Object getResponseData() {
        return response.getData();
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public void sendRedirect(String s) {
        if (s instanceof String) {
            response.setData("redirect:" + s);
        }
    }

}
