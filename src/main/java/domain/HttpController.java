package domain;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpController {

    private final HttpRequest httpRequest;
    private final String requestUrl;
    private final String requestPath;

    public HttpController(HttpRequest httpRequest, String requestUrl, String requestPath) {
        this.httpRequest = httpRequest;
        this.requestUrl = requestUrl;
        this.requestPath = requestPath;
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public String getCookie(String cookieName) {
        return httpRequest.getCookie(cookieName);
    }

    public HttpBody getHttpBody(BufferedReader bufferedReader) throws IOException {
        String httpBodyString = IOUtils.readData(bufferedReader, httpRequest.getContentLength());
        httpRequest.addHttpBody(HttpRequestUtils.parseQueryString(httpBodyString));
        return httpRequest.getHttpBody();
    }
}
