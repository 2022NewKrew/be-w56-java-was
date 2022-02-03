package http;

import java.util.HashMap;
import java.util.Map;

public enum MimeType {
    HTML("text/html", "html"),
    CSS("text/css", "css"),
    JAVASCRIPT("application/javascript", "js"),
    JPG("image/jpg", "jpg"),
    JPEG("image/jpeg", "jpeg"),
    PNG("image/png", "png"),
    ICON("image/x-icon", "ico"),
    TTF("application/x-font-ttf", "ttf"),
    WOFF("application/x-font-woff", "woff"),
    UNKNOWN("application/octet-stream", "unknown");

    private final String mimeType;
    private final String extension;

    public static final Map<String, MimeType> mimeTypeMap = new HashMap<>();

    static {
        for(MimeType mimeType : MimeType.values()) {
            mimeTypeMap.put(mimeType.getExtension(), mimeType);
        }
    }

    MimeType(String mimeType, String extension) {
        this.mimeType = mimeType;
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public String getMimeTypeString() {
        return mimeType;
    }

    public static MimeType getMimeTypeByExtension(String extension) {
        return mimeTypeMap.get(extension);
    }
}
