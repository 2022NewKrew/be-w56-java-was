package http.request;

import util.HttpRequestUtils;
import util.IOUtils;
import http.common.HttpHeaders;
import http.common.HttpVersion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequest {
    private final HttpMethod httpMethod;
    private final HttpUri httpUri;
    private final HttpQueries httpQueries;
    private final HttpVersion httpVersion;
    private final HttpHeaders httpHeaders;
    private final HttpRequestBody httpRequestBody;

    private HttpRequest(HttpMethod httpMethod, HttpUri httpUri, HttpQueries httpQueries, HttpVersion httpVersion, HttpHeaders httpHeaders, HttpRequestBody httpRequestBody) {
        this.httpMethod = httpMethod;
        this.httpUri = httpUri;
        this.httpQueries = httpQueries;
        this.httpVersion = httpVersion;
        this.httpHeaders = httpHeaders;
        this.httpRequestBody = httpRequestBody;
    }

    public static HttpRequest from(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String requestLine = br.readLine();
        String[] tokenizedRequestLine = HttpRequestUtils.tokenizeRequestLine(requestLine);
        HttpMethod httpMethod = HttpMethod.valueOf(tokenizedRequestLine[0]);
        String[] tokenizedRequestUri = HttpRequestUtils.tokenizeUriAndPath(tokenizedRequestLine[1]);
        HttpUri httpUri = new HttpUri(tokenizedRequestUri[0]);
        HttpQueries httpQueries = new HttpQueries(HttpRequestUtils.parseQueryString(tokenizedRequestUri[1]));
        HttpVersion httpVersion = HttpVersion.fromString(tokenizedRequestLine[2]);

        HttpHeaders httpHeaders = new HttpHeaders();

        String headerLine = br.readLine();
        while(headerLine != null && !headerLine.isEmpty()) {
            httpHeaders.parseHeaderLine(headerLine);
            headerLine = br.readLine();
        }

        HttpRequestBody httpRequestBody;
        try {
            int contentLength = Integer.parseInt(httpHeaders.getHeader("Content-Length"));
            httpRequestBody = new HttpRequestBody(IOUtils.readData(br, contentLength));
        } catch (NumberFormatException e) {
            httpRequestBody = HttpRequestBody.empty();
        }

        return new HttpRequest(httpMethod, httpUri, httpQueries, httpVersion, httpHeaders, httpRequestBody);
    }

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public String getHttpUri() {
        return httpUri.getValue();
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getQuery(String name) {
        return httpQueries.getQuery(name);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "httpMethod=" + httpMethod +
                ", httpUri=" + httpUri +
                ", httpQueries=" + httpQueries +
                ", httpVersion=" + httpVersion +
                ", httpHeaders=" + httpHeaders +
                ", httpRequestBody=" + httpRequestBody +
                '}';
    }
}
