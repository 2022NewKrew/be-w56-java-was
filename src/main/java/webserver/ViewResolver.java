package webserver;

import webserver.model.WebHttpRequest;
import webserver.model.WebHttpResponse;

import java.io.IOException;

public class ViewResolver {
    private static final ViewResolver INSTANCE = new ViewResolver();
    private final ViewRenderer renderer = ViewRenderer.getInstance();

    private ViewResolver() {
    }

    public static ViewResolver getInstance() {
        return INSTANCE;
    }

    public void resolve(WebHttpRequest httpRequest, WebHttpResponse httpResponse) {
        try {
            if (httpResponse.getResult() != null) {
                if (httpResponse.getResult().startsWith("redirect:")) {
                    renderer.redirect(httpResponse.getDos(), httpRequest.headers().map().get("Host").get(0) + httpResponse.getResult().split(":")[1]);
                } else {
                    renderer.render(httpResponse.getDos(), httpResponse.getResult());
                }
            } else {
                renderer.render(httpResponse.getDos(), httpRequest.uri().getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
