package webserver;

import exception.InvalidViewNameException;
import http.HttpStatus;
import http.MediaType;
import http.response.HttpResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

public class ModelAndView {

    private static final String REDIRECT = "redirect";
    private static final String REDIRECT_PATH_DELIMITER = ":";
    private static final String TEXT_HTML_UTF8 = "text/plain; charset=utf-8";

    private final String viewName;
    private final HttpStatus status;
    private final Model model;

    private ModelAndView(String viewName, HttpStatus status, Model model) {
        this.viewName = viewName;
        this.status = status;
        this.model = model;
    }

    public static ModelAndView from(String viewName) {
        return of(viewName, new Model());
    }

    public static ModelAndView of(String viewName, Model model) {
        if (viewName.contains(REDIRECT)) {
            String[] redirectViewName = viewName.split(REDIRECT_PATH_DELIMITER);
            if (redirectViewName.length != 2) {
                throw new InvalidViewNameException(viewName);
            }

            return new ModelAndView(redirectViewName[1], HttpStatus.FOUND, model);
        }

        return new ModelAndView(viewName, HttpStatus.OK, model);
    }

    public static ModelAndView error(HttpStatus status) {
        return new ModelAndView("", status, new Model());
    }

    public void render(HttpResponse httpResponse, TemplateView templateView)
            throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        byte[] body = getBody(templateView);
        setResponse(httpResponse, body);
    }

    private byte[] getBody(TemplateView templateView) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (!getContentType().contains("text")) {
            return templateView.loadStaticFile(viewName);
        }
        if (status == HttpStatus.OK) {
            return templateView.renderTemplateModel(viewName, model);
        }
        if (status == HttpStatus.FOUND) {
            return null;
        }

        return status.getErrorMessage().getBytes(StandardCharsets.UTF_8);
    }

    private void setResponse(HttpResponse httpResponse, byte[] body) {
        setResponseHeaders(httpResponse, body);
        setResponseBody(httpResponse, body);
        setResponseStatus(httpResponse);
    }

    private void setResponseHeaders(HttpResponse httpResponse, byte[] body) {
        if (body == null && status == HttpStatus.FOUND) {
            httpResponse.setLocation(viewName);
            return;
        }

        httpResponse.setContentType(getContentType());
        if (body != null) {
            httpResponse.setContentLength(String.valueOf(body.length));
        }
    }

    private String getContentType() {
        if (status != HttpStatus.OK && status != HttpStatus.FOUND) {
            return TEXT_HTML_UTF8;
        }
        return MediaType.getMediaType(viewName).getType();
    }

    private void setResponseStatus(HttpResponse httpResponse) {
        httpResponse.setStatus(status);
    }

    private void setResponseBody(HttpResponse httpResponse, byte[] body) {
        if (body == null) {
            httpResponse.setBody(new byte[0]);
            return;
        }
        httpResponse.setBody(body);
    }
}
