package webserver.http.message.values;

public enum HttpContentType {

    TEXT_CSS(".css", "text/css;charset=utf-8"),
    TEXT_HTML(".html", "text/html;charset=utf-8"),
    APPLICATION_JS(".js", "application/js"),
    IMAGE_X_ICON(".ico", "image/x-icon"),
    ;

    private final String extension;
    private final String contentType;

    HttpContentType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getValue() {
        return contentType;
    }

    public static HttpContentType getHttpContentType(String fileName) {
        for(HttpContentType type : HttpContentType.values()) {
            if(fileName.contains(type.extension)) {
                return type;
            }
        }
        return TEXT_HTML;
    }
}
