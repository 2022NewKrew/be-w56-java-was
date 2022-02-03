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

        if (tokens[1].contains("?")) { // queryString 존재
            queryString = HttpRequestUtils.parseQueryString(tokens[1].split("\\?")[1]);
        }

        return new HttpRequestLine(tokens[0], tokens[1], queryString, tokens[2]);
    }

    public String method() { return method; }

    public String path() { return path; }

    public String methodAndPath() { return method + " " + path; }

    public Map<String, String> queryString() { return queryString; }
}
