package webserver;

import webserver.model.WebHttpRequest;
import webserver.model.WebHttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;

public class ViewResolver {
    private static final ViewResolver INSTANCE = new ViewResolver();
    private final ViewRenderer renderer = ViewRenderer.getInstance();

    private ViewResolver() {
    }

    public static ViewResolver getInstance() {
        return INSTANCE;
    }

    public void resolve(WebHttpRequest httpRequest, WebHttpResponse httpResponse, DataOutputStream dos) {
        try {
            switch (httpResponse.getHttpStatus()) {
                case OK:
                    renderer.responseResource(dos, httpResponse);
                    break;
                case FOUND:
                    renderer.redirect(dos, httpResponse);
                    break;
                default:
                    renderer.responseResource(dos, httpResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
