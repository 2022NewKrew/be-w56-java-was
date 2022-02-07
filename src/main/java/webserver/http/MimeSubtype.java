package webserver.http;

public enum MimeSubtype {
    TEXT_HTML("text/html; charset=UTF-8"),
    TEXT_CSS("text/css"),
    APPLICATION_JSON("app/json"),
    TEXT_JS("text/javascript"),
    X_ICON("image/x-icon"),
    FONT_WOFF("font/woff"),
    FONT_TTF("font/ttf");

    private final String value;

    MimeSubtype(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return value;
    }
}
