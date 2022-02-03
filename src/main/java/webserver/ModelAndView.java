package webserver;

import webserver.web.response.Response;

public class ModelAndView {

    private final String result;
    private final Model model;
    private final Response.ResponseBuilder builder;

    public ModelAndView(String result, Model model, Response.ResponseBuilder builder) {
        this.result = result;
        this.model = model;
        this.builder = builder;
    }

    public View getView() {
        return new View(result, model, builder);
    }
}
