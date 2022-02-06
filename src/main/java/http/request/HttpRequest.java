package http.request;

import http.common.Headers;
import http.common.HttpVersion;

public class HttpRequest {
    private final HttpMethod httpMethod;
    private final Uri uri;
    private final Queries queries;
    private final HttpVersion httpVersion;
    private final Headers headers;
    private final RawRequestBody rawRequestBody;
    private final RequestBody requestBody;

    HttpRequest(HttpMethod httpMethod, Uri uri, Queries queries, HttpVersion httpVersion, Headers headers, RawRequestBody rawRequestBody, RequestBody requestBody) {
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.queries = queries;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.rawRequestBody = rawRequestBody;
        this.requestBody = requestBody;
    }

    public String getQuery(String name) {
        return queries.getQuery(name);
    }

    public String getBody(String name) {
        return requestBody.getBody(name);
    }

    public HttpVersion getVersion() {
        return httpVersion;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public Uri getUri() {
        return uri;
    }
}
