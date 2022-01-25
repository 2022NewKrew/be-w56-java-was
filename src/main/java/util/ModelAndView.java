package util;


public class ModelAndView {
    private String viewName;
    private int responseCode;

    public ModelAndView() {
    }

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public ModelAndView(String viewName, int responseCode) {
        this.viewName = viewName;
        this.responseCode = responseCode;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
