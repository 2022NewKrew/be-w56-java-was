package mvcframework;

public class HandlerKey {
    private final String url;
    private final RequestMethod requestMethod;

    public HandlerKey(String url, RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public String getUrl() {
        return url;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (url == null ? 0 : url.hashCode());
        hash = 31 * hash + (requestMethod == null ? 0 : requestMethod.hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HandlerKey other)) {
            return false;
        }
        return this.url.equals(other.getUrl()) && this.requestMethod.equals(other.getRequestMethod());
    }

    @Override
    public String toString() {
        return "HandlerKey [url=" + url + ", requestMethod=" + requestMethod + "]";
    }
}
