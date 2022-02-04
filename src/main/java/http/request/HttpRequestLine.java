package http.request;

import util.HttpRequestUtils;

import java.util.Map;

public class HttpRequestLine {
    private final String method;
    private final String path;
    private final Map<String, String> queryString;
    private final String httpVersion;

    public HttpRequestLine(String method, String path, Map<String, String> queryString, String httpVersion) {
        this.method = method;
        this.path = path;
        this.queryString = queryString;
        this.httpVersion = httpVersion;
    }

    public static HttpRequestLine parseRequestLine(final String request) {
        String[] tokens = request.split(" ");
        Map<String, String> queryString = null;
        String path = tokens[1];
        int q = path.lastIndexOf("?");

        if (q != -1) { // queryString 존재
            path = path.substring(0, q);
            queryString = HttpRequestUtils.parseQueryString(tokens[1].substring(q+1));
        }

        return new HttpRequestLine(tokens[0], path, queryString, tokens[2]);
    }

    public String method() { return method; }

    public String path() { return path; }

    public String methodAndPath() { return method + " " + path; }

    public Map<String, String> queryString() { return queryString; }
}
