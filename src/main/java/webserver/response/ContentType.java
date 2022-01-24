package webserver.response;

import webserver.exception.ContentTypeNotFoundedException;

public enum ContentType {

    TEXT_HTML("text/html", "html"),
    TEXT_CSS("text/css", "css"),
    TEXT_JS("text/javascript", "js"),
    IMAGE_PNG("image/png", "png"),
    IMAGE_JPEG("image/jpeg", "jpeg"),
    IMAGE_X_ICON("image/x-icon", "ico");

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
        throw new ContentTypeNotFoundedException();
    }
}
