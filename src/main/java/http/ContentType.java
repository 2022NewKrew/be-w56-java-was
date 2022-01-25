package http;

import javax.annotation.Nullable;

public enum ContentType {
    HTML(".html", "text/html"),
    CSS(".css", "text/css"),
    JAVASCRIPT(".js", "text/javascript"),
    PNG(".png", "image/png"),
    GIF(".gif", "image/gif"),
    JPEG(".jpeg", "image/jpeg"),
    JSON(".json", "application/json"),
    TEXT(".txt", "text/plain"),
    XML(".xml", "application/xml"),
    PDF(".pdf", "application/pdf"),
    OCTET_STREAM(".bin", "application/octet-stream");

    private final String extension;
    private final String contentType;

    ContentType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getExtension() {
        return extension;
    }

    public String getContentType() {
        return contentType;
    }

    @Nullable
    public static ContentType fromExtension(String extension) {
        for (ContentType contentType : values()) {
            if (contentType.getExtension().equals(extension)) {
                return contentType;
            }
        }
        return null;
    }
}
