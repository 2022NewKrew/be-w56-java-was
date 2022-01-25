package http;

public class HeaderBuilder {
    private String accept;
    private String acceptEncoding;
    private String acceptLanguage;
    private String cacheControl;
    private String connection;
    private String cookie;
    private String host;
    private String referer;
    private String userAgent;

    public HeaderBuilder setAccept(String accept) {
        this.accept = accept;
        return this;
    }

    public HeaderBuilder setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
        return this;
    }

    public HeaderBuilder setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
        return this;
    }

    public HeaderBuilder setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
        return this;
    }

    public HeaderBuilder setConnection(String connection) {
        this.connection = connection;
        return this;
    }

    public HeaderBuilder setCookie(String cookie) {
        this.cookie = cookie;
        return this;
    }

    public HeaderBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public HeaderBuilder setReferer(String referer) {
        this.referer = referer;
        return this;
    }

    public HeaderBuilder setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public Header build() {
        return new Header(accept, acceptEncoding, acceptLanguage, cacheControl, connection, cookie, host, referer, userAgent);
    }
}
