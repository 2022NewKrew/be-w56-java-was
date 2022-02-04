package framework;

import framework.constant.HttpStatusCode;
import framework.params.HttpResponse;
import framework.params.Params;

import java.io.IOException;
import java.nio.file.Files;

/**
 * FrontController로부터 View 이름을 전달받아 View를 검색 (Response를 생성)
 */
public class ViewResolver {
    private final View view;

    public ViewResolver(View view) {
        this.view = view;
    }

    public HttpResponse getResponse(String viewName, Params params) throws IOException {
        if (viewName.contains("redirect")) {
            return getResponse3xx(HttpStatusCode.REDIRECT, viewName, params);
        }
        String viewFileName = addExtension(viewName);
        return getResponse2xx(HttpStatusCode.OK, viewFileName, params);
    }

    private String addExtension(String viewName) {
        return viewName.contains(".") ? viewName : viewName + ".html";
    }

    private HttpResponse getResponse2xx(HttpStatusCode httpStatusCode, String viewFileName, Params params) throws IOException {
        HttpResponse httpResponse = new HttpResponse(httpStatusCode, params.session);
        view.setResponseBody(httpResponse, viewFileName, params.model);
        return httpResponse;
    }

    private HttpResponse getResponse3xx(HttpStatusCode httpStatusCode, String viewFileName, Params params) {
        String redirectPath = viewFileName.split(":")[1];
        HttpResponse httpResponse = new HttpResponse(httpStatusCode, params.session);
        httpResponse.setRedirectUrl(redirectPath);
        return httpResponse;
    }
}
