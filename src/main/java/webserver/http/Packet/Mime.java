package webserver.http.Packet;

import java.util.Arrays;

public enum Mime {

    HTML("text/html", ".html"),
    CSS("text/css", ".css"),
    JPEG("image/jpeg", ".jpg"),
    PNG("image/png", ".png"),
    JS("application/javascript", ".js"),
    ICO("image/x-icon", ".ico"),
    TTF("application/x-font-ttf", ".ttf"),
    WOFF("application/x-font-woff", ".woff");

    private final String contentType;
    private final String extension;

   Mime(String contentType, String extension) {
        this.contentType = contentType;
        this.extension = extension;
    }

    public static String parse(String path) {
        Mime mime = Arrays.stream(values())
                .filter(m -> path.endsWith(m.extension))
                .findFirst().orElse(HTML);

        return mime.contentType;
    }

    public boolean isExtensionMatch(String path) {
        return path.endsWith(extension);
    }

    public String getExtension() {
        return extension;
    }
}
