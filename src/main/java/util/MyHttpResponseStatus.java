package util;

public enum MyHttpResponseStatus {
    OK("200"), NOT_FOUND("404");

    private final String value;

    MyHttpResponseStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
