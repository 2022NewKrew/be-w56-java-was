package infrastructure.model;

import java.util.Arrays;

public enum ContentType {
    HTML(".html", "text/html", true),
    ICO(".ico", "text/plain", true),
    CSS(".css", "text/css", true),
    JS(".js", "application/js", true),
    UNKNOWN("text/html", true);

    private final String extension;
    private final String mimeType;
    private final boolean isDiscrete;

    ContentType(String mimeType, boolean isDiscrete) {
        this.extension = "";
        this.mimeType = mimeType;
        this.isDiscrete = isDiscrete;
    }

    ContentType(String extension, String responseType, boolean isDiscrete) {
        this.extension = extension;
        this.mimeType = responseType;
        this.isDiscrete = isDiscrete;
    }

    public static ContentType valueOfPath(String path) {
        return Arrays.stream(values())
                .filter(e -> path.endsWith(e.getExtension()))
                .findAny()
                .orElse(UNKNOWN);
    }

    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public boolean isDiscrete() {
        return isDiscrete;
    }
}
