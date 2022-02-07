package webserver.http.domain;

import webserver.http.request.HttpRequest;

public class MethodAndUrl {
    private String method;
    private String url;

    public MethodAndUrl(HttpMethod method, String url) {
        this.method = method.toString();
        this.url = url;
    }

    public boolean checkRequest(HttpRequest req) {
        return req.getRequestUri().equals(url) && req.getMethod().equals(method);
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int hashCode() {
        int result = 17;
        if (method != null) {
            result = 31 * result + method.hashCode();
        }
        if (url != null) {
            result = 31 * result + url.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof MethodAndUrl)) return false;
        MethodAndUrl o = (MethodAndUrl) obj;
        return o.getMethod().equals(method) && o.getUrl().equals(url);
    }
}
