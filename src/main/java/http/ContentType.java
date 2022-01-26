package http;

public enum ContentType {
    CSS("text/css"),
    JS("text/javascript"),
    HTML("text/html"),
    DEFAULT("application/octet-stream");

    private String str;

    ContentType(String str) {
        this.str = str;
    }

    public String str() { return str; }
}
