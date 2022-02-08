package model;

import java.util.Objects;

public class RequestData {
    private final String method;
    private final String urlPath;
    private final String urlQuery;
    private final String httpVersion;

    public RequestData(String method, String urlPath, String urlQuery, String httpVersion) {
        this.method = method;
        this.urlPath = urlPath;
        this.urlQuery = urlQuery;
        this.httpVersion = httpVersion;
    }

    public String getMethod() {
        return method;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public String getUrlQuery() {
        return urlQuery;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestData that = (RequestData) o;
        return method.equals(that.method) && urlPath.equals(that.urlPath) && urlQuery.equals(that.urlQuery) && httpVersion.equals(that.httpVersion);
    }

    @Override
    public String toString() {
        return "RequestData{" +
                "method='" + method + '\'' +
                ", urlPath='" + urlPath + '\'' +
                ", urlQuery='" + urlQuery + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, urlPath, urlQuery, httpVersion);
    }
}
