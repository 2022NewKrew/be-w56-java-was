package util.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public enum ContentType {
    PLANE_TEXT("plain", "text/plain", true),
    HTML("html", "text/html", true),
    CSS("css", "text/css", true),
    JS("js", "text/javascript", true),
    SVG("svg", "image/svg+xml",false),
    PNG("png","image/png", false),
    WOFF("woff", "*/*", false),
    TTF("ttf", "*/*", false),
    ICO("ico", "*/*", false);

    private final String extensionName;
    private final String acceptName;
    private final boolean isText;

    public static Optional<ContentType> getFileType(String fileName){
        String[] split = fileName.split("\\.");
        String extensionName = split[split.length-1];

        return Arrays.stream(ContentType.values())
                .filter(contentType -> contentType.extensionName.equals(extensionName))
                .findFirst();
    }
}
