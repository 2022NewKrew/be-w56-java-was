package util;

public class RedirectPair {
    private final String url;
    private final boolean isRedirect;

    public RedirectPair(String url, boolean isRedirect) {
        this.url = url;
        this.isRedirect = isRedirect;
    }

    public String getUrl() {
        return url;
    }

    public boolean isRedirect() {
        return isRedirect;
    }
}
