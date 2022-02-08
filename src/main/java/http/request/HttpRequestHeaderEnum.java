package http.request;

public enum HttpRequestHeaderEnum {

    ACCEPT("Accept"),
    CONNECTION("Connection"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    COOKIE("Cookie"),
    ;

    private final String text;

    HttpRequestHeaderEnum(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
