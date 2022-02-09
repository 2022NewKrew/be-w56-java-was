package webapplication.dto;

import webapplication.data.Model;

public class ModelAndView {

    private final String viewName;

    private Model model;

    public ModelAndView(String viewName) {
        this.viewName = viewName;
        this.model = new Model();
    }

    public void addAttribute(String attributeName, Object attributeValue) {
        model.addAttribute(attributeName, attributeValue);
    }

    public String getViewName() {
        return viewName;
    }

    public Model getModel() {
        return model;
    }

    public Boolean isRedirect() {
        return viewName.startsWith("redirect:");
    }

    public Boolean isCookies() {
        return model.isCookies();
    }

}
