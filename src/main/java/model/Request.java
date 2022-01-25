package model;

public class Request {

    private String method;
    private String url;
    private String query;
    private String httpVersion;

    public Request(String method, String url, String httpVersion) {
        this.method = method;
        this.url = url;
        this.httpVersion = httpVersion;
    }

    public Request(String method, String url, String query, String httpVersion) {
        this(method, url, httpVersion);
        this.query = query;
    }

    public String getUrl() {
        return url;
    }

    public String getQuery() {
        return query;
    }
}
