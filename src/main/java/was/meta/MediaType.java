package was.meta;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public enum MediaType {

    IMAGE_X_ICON("image", "x-icon", "ico"),
    TEXT_CSS("text", "css", "css"),
    TEXT_HTML("text", "html", "htm[l]?"),
    TEXT_PLAIN("text", "plain", "(.*)"),
    APPLICATION_FORM_URLENCODED("application", "x-www-form-urlencoded", "(.*)"),
    ALL("*", "*", "(.*)");

    private final String type;

    private final String subtype;

    private final String charset;

    private final String extension;

    MediaType(String type, String subtype, String extension) {
        this.type = type;
        this.subtype = subtype;
        this.charset = "charset=UTF-8";
        this.extension = extension;
    }

    public String getValue() {
        return type + "/" + subtype;
    }

    public static MediaType getMediaType(String path, String acceptLine) {
        int index = path.lastIndexOf('.');
        String fileExtension = (index > 0) ? path.substring(index + 1) : path;

        MediaType extensionMatchedMediaType = EnumSet.allOf(MediaType.class).stream()
                .filter(mediaType -> mediaType.matchExtension(fileExtension))
                .findFirst()
                .orElse(null);

        if (extensionMatchedMediaType != null) {
            return extensionMatchedMediaType;
        }

        List<MediaType> acceptableMediaTypes = parseMediaTypes(acceptLine);

        return acceptableMediaTypes.stream()
                .filter(EnumSet.allOf(MediaType.class)::contains)
                .findFirst()
                .orElse(MediaType.ALL);
    }

    public static List<MediaType> parseMediaTypes(String acceptLine) {
        String[] acceptTokens = acceptLine.split(",");
        return Arrays.stream(acceptTokens)
                .map(MediaType::parseMediaType)
                .collect(Collectors.toList());
    }

    public static MediaType parseMediaType(String acceptToken) {
        int index = acceptToken.indexOf(';');
        String fullType = (index >= 0 ? acceptToken.substring(0, index) : acceptToken).trim();

        int subIndex = acceptToken.indexOf('/');
        String type = fullType.substring(0, subIndex);
        String subtype = fullType.substring(subIndex + 1);

        return EnumSet.allOf(MediaType.class).stream()
                .filter(mediaType -> mediaType.matchTypeAndSubtype(type, subtype))
                .findFirst()
                .orElse(MediaType.TEXT_PLAIN);
    }

    private boolean matchTypeAndSubtype(String type, String subtype) {
        return this.type.equals(type) && this.subtype.equals(subtype);
    }

    private boolean matchExtension(String fileExtension) {
        return fileExtension.matches(extension);
    }
}
