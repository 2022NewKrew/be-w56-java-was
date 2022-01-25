package http;

public class Request {

    private String httpMethod;
    private String path;
    private String queryString;
    private String httpVersion;

    public Request(String httpMethod, String path, String queryString, String httpVersion) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.queryString = queryString;
        this.httpVersion = httpVersion;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
