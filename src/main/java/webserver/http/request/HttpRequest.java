package webserver.http.request;

import static webserver.http.HttpMeta.NO_SESSION;
import static webserver.http.HttpMeta.SESSION_ID_IN_COOKIE;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import util.HttpRequestUtils;
import webserver.http.request.exceptions.NullRequestException;
import webserver.http.request.exceptions.RequestBuilderException;

public class HttpRequest {

    private final HttpRequestLine requestLine;
    private final HttpRequestHeaders requestHeaders;
    private final HttpRequestBody requestBody;

    public HttpRequest(Builder builder) {
        this.requestLine = builder.requestLine;
        this.requestHeaders = builder.requestHeaders;
        this.requestBody = builder.requestBody;
    }

    public String getRequestBody() {
        return requestBody.getBody();
    }

    public String getHttpVersion() {
        return requestLine.getHttpVersion();
    }

    public URI getUri() {
        return requestLine.getUri();
    }

    public Method getMethod() {
        return requestLine.getMethod();
    }

    public Map<String, String> getQueryData() {
        Method method = getMethod();
        return method == Method.GET ? getQueryDataFromUri() : getQueryDataFromBody();
    }

    private Map<String, String> getQueryDataFromUri() {
        URI uri = getUri();
        return HttpRequestUtils.parseQueryString(uri.getQuery());
    }

    private Map<String, String> getQueryDataFromBody() {
        String body = getRequestBody();
        return HttpRequestUtils.parseQueryString(body);
    }

    public int getSessionId() {
        String cookieStr = requestHeaders.getCookie();
        Map<String, String> cookie = HttpRequestUtils.parseCookies(cookieStr);
        String sessionIdStr = cookie.get(SESSION_ID_IN_COOKIE);
        if (sessionIdStr == null) {
            return NO_SESSION;
        }
        return Integer.parseInt(sessionIdStr);
    }

    public void checkRequestValidation() throws NullRequestException {
        if (requestLine == null) {
            throw new NullRequestException();
        }
    }

    public static class Builder {

        private HttpRequestLine requestLine;
        private HttpRequestHeaders requestHeaders;
        private HttpRequestBody requestBody;

        public Builder requestLine(BufferedReader br) throws IOException {
            this.requestLine = HttpRequestLine.createRequestLineFromBufferedReader(br);
            return this;
        }

        public Builder requestHeaders(BufferedReader br) throws IOException {
            this.requestHeaders = HttpRequestHeaders.createRequestHeadersFromBufferedReader(br);
            return this;
        }

        public Builder requestBody(BufferedReader br) throws IOException, RequestBuilderException {
            if (requestHeaders == null) {
                throw new RequestBuilderException();
            }
            this.requestBody = HttpRequestBody.createRequestBodyFromBufferedReader(
                br,
                requestHeaders.getContentLength()
            );
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}
