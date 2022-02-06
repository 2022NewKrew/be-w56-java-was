package request;

public enum ReqHeader {

    ACCEPT("Accept"),
    CONNECTION("Connection"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    COOKIE("Cookie"),
    ;

    private final String text;

    ReqHeader(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
