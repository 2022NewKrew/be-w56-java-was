package http.header;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MimeType {
    HTML(".html", "text/html"),
    CSS(".css", "text/css"),
    JS(".js", "text/javascript"),
    PNG(".png", "image/png"),
    EOT(".eot", "application/vnd.ms-fontobject"),
    SVG(".svg", "image/svg+xml"),
    TTF(".ttf", "font/ttf"),
    WOFF(".woff", "font/woff"),
    WOFF2(".woff2", "font/woff2"),
    NONE(null, "");

    private final String extension;
    private final String mimeType;

    MimeType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public static MimeType matchOf(String extension) {
        List<MimeType> match = Arrays.stream(values())
                .filter(type -> extension.equals(type.extension))
                .collect(Collectors.toList());

        if (match.isEmpty()) {
            return NONE;
        }
        return match.get(0);
    }

    public String getMimeType() {
        return mimeType;
    }
}
