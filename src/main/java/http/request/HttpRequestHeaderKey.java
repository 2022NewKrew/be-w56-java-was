package http.request;

public enum HttpRequestHeaderKey {

    ACCEPT("Accept"),
    CONNECTION("Connection"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    COOKIE("Cookie"),
    ;

    private final String text;

    HttpRequestHeaderKey(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
