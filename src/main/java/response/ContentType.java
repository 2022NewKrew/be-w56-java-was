package response;

public enum ContentType {
    CSS("text/css"),
    JS("text/javascript"),
    HTML("text/html"),
    DEFAULT("application/octet-stream");

    private final String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }

    public String value() { return contentType; }
}
