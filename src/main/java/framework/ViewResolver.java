package framework;

import framework.variable.HttpStatusCode;
import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static framework.variable.PathVariable.STATIC_RESOURCE_BASE_URL;

/**
 * FrontController로부터 View 이름을 전달받아 View를 검색 (Response를 생성)
 */
public class ViewResolver {

    public HttpResponse getResponse(String viewName) throws IOException {
        if (viewName.contains("redirect")) {
            return getResponse3xx(HttpStatusCode.REDIRECT, viewName);
        }
        String viewFileName = addExtension(viewName);
        return getResponse2xx(HttpStatusCode.OK, viewFileName);
    }

    private String addExtension(String viewName) {
        if (!viewName.contains(".")) {
            viewName += ".html";
        }
        return viewName;
    }

    private HttpResponse getResponse2xx(HttpStatusCode httpStatusCode, String viewFileName) throws IOException {
        File file = new File(STATIC_RESOURCE_BASE_URL.getPath() + viewFileName);
        HttpResponse httpResponse = new HttpResponse(httpStatusCode);
        httpResponse.setMimeType(getMimeType(file));
        httpResponse.setBody(Files.readAllBytes(file.toPath()));
        return httpResponse;
    }

    private HttpResponse getResponse3xx(HttpStatusCode httpStatusCode, String viewFileName) {
        String redirectPath = viewFileName.split(":")[1];
        HttpResponse httpResponse = new HttpResponse(httpStatusCode);
        httpResponse.setRedirectUrl(redirectPath);
        return httpResponse;
    }

    private String getMimeType(File file) throws IOException {
        Tika tika = new Tika();
        return tika.detect(file);
    }
}
