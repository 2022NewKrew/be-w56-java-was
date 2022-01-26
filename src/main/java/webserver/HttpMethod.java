package webserver;

public enum HttpMethod {
    GET, POST;

    public boolean isGET() {
        return this == GET;
    }

    public boolean isPOST() {
        return this == POST;
    }
}
