package webserver.response;

public enum ContentType {

    TEXT_HTML("text/html", "html"),
    TEXT_CSS("text/css", "css"),
    TEXT_JS("text/javascript", "js"),
    IMAGE_PNG("image/png", "png"),
    IMAGE_JPEG("image/jpeg", "jpeg"),
    IMAGE_X_ICON("image/x-icon", "ico"),
    APPLICATION_JSON("application/json", "json"),
    APPLICATION_OCTET_STREAM("application/octet-stream", "");

    private String value;
    private String extension;

    ContentType(String value, String extension) {
        this.value = value;
        this.extension = extension;
    }

    public String getValue() {
        return value;
    }

    public static ContentType getContentTypeByExtension(String extension) {
        for (ContentType contentType : ContentType.values()) {
            if (contentType.extension.equals(extension)) {
                return contentType;
            }
        }
        return APPLICATION_OCTET_STREAM;
    }
}
