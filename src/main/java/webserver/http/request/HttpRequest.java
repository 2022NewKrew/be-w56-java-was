package webserver.http.request;

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
