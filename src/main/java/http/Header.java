package http;

public class Header {
    private String accept;
    private String acceptEncoding;
    private String acceptLanguage;
    private String cacheControl;
    private String connection;
    private String cookie;
    private String host;
    private String referer;
    private String userAgent;

    public Header(String accept, String acceptEncoding, String acceptLanguage, String cacheControl, String connection, String cookie, String host, String referer, String userAgent) {
        this.accept = accept;
        this.acceptEncoding = acceptEncoding;
        this.acceptLanguage = acceptLanguage;
        this.cacheControl = cacheControl;
        this.connection = connection;
        this.cookie = cookie;
        this.host = host;
        this.referer = referer;
        this.userAgent = userAgent;
    }

    public String getAccept() {
        return accept;
    }

    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    public String getConnection() {
        return connection;
    }

    public String getCookie() {
        return cookie;
    }

    public String getHost() {
        return host;
    }

    public String getReferer() {
        return referer;
    }

    public String getUserAgent() {
        return userAgent;
    }
}
