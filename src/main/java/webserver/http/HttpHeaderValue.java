package webserver.http;

public enum HttpHeaderValue {
    TEXT_HTML("text/html; charset=UTF-8"),
    TEXT_CSS("text/css"),
    APPLICATION_JSON("application/json"),
    APPLICATION_JS("application/javascript"),
    ;

    private final String value;

    HttpHeaderValue(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return value;
    }
}
