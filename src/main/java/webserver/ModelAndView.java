package webserver;

import exception.InvalidViewNameException;
import http.HttpStatus;
import java.util.Map;

public class ModelAndView {

    private static final String REDIRECT = "redirect";
    private static final String REDIRECT_PATH_DELIMITER = ":";

    private final String viewName;
    private final HttpStatus status;
    private Map<String, Object> model;

    public ModelAndView(String viewName, HttpStatus status) {
        this.viewName = viewName;
        this.status = status;
    }

    public ModelAndView(String viewName, HttpStatus status, Map<String, Object> model) {
        this.viewName = viewName;
        this.model = model;
        this.status = status;
    }

    public static ModelAndView from(String viewName) {
        if (viewName.contains(REDIRECT)) {
            String[] redirectViewName = viewName.split(REDIRECT_PATH_DELIMITER);
            if (redirectViewName.length != 2) {
                throw new InvalidViewNameException(viewName);
            }

            return new ModelAndView(redirectViewName[1], HttpStatus.FOUND);
        }

        return new ModelAndView(viewName, HttpStatus.OK);
    }

    public static ModelAndView error(HttpStatus status) {
        return new ModelAndView("", status);
    }

    public String getViewName() {
        return viewName;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
