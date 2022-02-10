package webserver.response;

public enum ContentType {
    DEFAULT("Multipart/related"),
    TEXT_HTML("text/html"),
    APPLICATION_JSON("Application/json"),
    MULTIPART_FORMED_DATA("multipart/formed-data"),
    ;

    private String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getType() {
        return contentType;
    }
}
