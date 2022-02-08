package http.response;

public enum HttpResponseHeaderEnum {

    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    LOCATION("Location"),
    SET_COOKIE("Set-Cookie"),
    ;

    private final String text;

    HttpResponseHeaderEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
