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

    public void setValue(String fieldName, String value) {
        if (fieldName.equals("Accept")) {
            this.accept = value;
        }
        if (fieldName.equals("Accept-Encoding")) {
            this.acceptEncoding = value;
        }
        if (fieldName.equals("Accept-Language")) {
            this.acceptLanguage = value;
        }
        if (fieldName.equals("Cache-Control")) {
            this.cacheControl = value;
        }
        if (fieldName.equals("Connection")) {
            this.connection = value;
        }
        if (fieldName.equals("Cookie")) {
            this.cookie = value;
        }
        if (fieldName.equals("Host")) {
            this.host = value;
        }
        if (fieldName.equals("Referer")) {
            this.referer = value;
        }
        if (fieldName.equals("User-Agent")) {
            this.userAgent = value;
        }
    }

}
