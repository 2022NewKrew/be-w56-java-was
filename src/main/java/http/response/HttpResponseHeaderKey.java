package http.response;

public enum HttpResponseHeaderKey {

    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    LOCATION("Location"),
    SET_COOKIE("Set-Cookie"),
    ;

    private final String text;

    HttpResponseHeaderKey(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
