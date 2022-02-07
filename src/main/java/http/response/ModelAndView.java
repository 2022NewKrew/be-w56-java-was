package http.response;

public class ModelAndView {
    private String viewName;
    private Model model;

    public ModelAndView(String viewName) {
        this.viewName = viewName;
        this.model = new Model();
    }

    public ModelAndView(String viewName, Model model) {
        this.viewName = viewName;
        this.model = model;
    }

    public boolean hasModel() {
        return !model.isEmpty();
    }

    public boolean isRedirect() {
        return viewName.contains("redirect:");
    }

    public Object getAttr(String key) {
        return model.getAttr(key);
    }

    public String getViewName() {
        return viewName;
    }

    public String getRedirectUrl() {
        return viewName.replace("redirect:", "");
    }
}
