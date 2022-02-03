package webserver.framwork.core.view;

import webserver.framwork.http.response.HttpResponse;
import webserver.framwork.http.response.HttpStatus;

public class RedirectView implements View {
    private static final String redirectContentType = "text/html";

    private final String redirectUrl;

    public RedirectView(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public String getContentType() {
        return redirectContentType;
    }

    @Override
    public void render(HttpResponse response) {
        response.setStatus(HttpStatus.Redirect);
        response.addHeaderValue("Location", redirectUrl);
    }
}
