package webserver.model;

import webserver.http.HttpStatus;

public class ModelAndView {
    private final String viewPath;
    private final HttpStatus status;

    public ModelAndView(String viewPath, HttpStatus status) {
        this.viewPath = viewPath;
        this.status = status;
    }

    public ModelAndView(String viewPath) {
        this.viewPath = viewPath;
        this.status = HttpStatus.OK;
    }

    public String getViewPath() {
        return viewPath;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }

    public boolean isRedirectView() {
        return status.equals(HttpStatus.FOUND);
    }

}
