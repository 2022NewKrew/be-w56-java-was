package domain;

import com.google.common.base.Enums;

public enum ContentType {
    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "application/javascript"),
    JSON("json", "application/json"),
    ICO("ico", "image/x-icon");

    private final String fileExtension;
    private final String contentType;

    ContentType(String fileExtension, String contentType) {
        this.fileExtension = fileExtension;
        this.contentType = contentType;
    }

    public static ContentType getIfPresent(String fileExtension) {
       return Enums.getIfPresent(ContentType.class, fileExtension).or(HTML);
    }

    public String getContentType() {
        return contentType;
    }
}
