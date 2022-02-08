package was.http.meta;

import java.util.List;

public enum MediaTypes {
    IMAGE_X_ICON("ico", "image/x-icon"),
    TEXT_HTML("htm[l]?", "text/html;charset=UTF-8"),
    TEXT_PLAIN("(.*)", "text/plain;charset=UTF-8"),
    APPLICATION_X_WWW_FORM_URLENCODED("(.*)", "application/x-www-form-urlencoded"),
    TEXT_CSS("css", "text/css"),
    APPLICATION_JS("js", "application/js"),
    APPLICATION_JSON("json", "application/json");

    private final String regex;
    private final String value;

    MediaTypes(String regex, String value) {
        this.regex = regex;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MediaTypes findMediaType(List<String> AcceptToken, String fileExtension) {
        for (MediaTypes mediaTypes : MediaTypes.values()) {
            if (fileExtension.matches(mediaTypes.regex) && AcceptToken.stream().anyMatch(mediaTypes.value::matches)) {
                return mediaTypes;
            }
        }
        return TEXT_PLAIN;
    }
}
