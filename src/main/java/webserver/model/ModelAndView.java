package webserver.model;

import lombok.Getter;
import webserver.http.HttpStatus;

@Getter
public class ModelAndView {
    private final String viewPath;
    private final HttpStatus httpStatus;
    private final Model model;

    public ModelAndView(String viewPath, HttpStatus httpStatus) {
        this.viewPath = viewPath;
        this.httpStatus = httpStatus;
        this.model = new Model();
    }

    public ModelAndView(String viewPath) {
        this(viewPath, HttpStatus.OK);
    }

    public boolean isRedirectView() {
        return httpStatus.equals(HttpStatus.FOUND);
    }

    public void addAttribute(String name, Object value) {
        model.addAttribute(name, value);
    }

}
