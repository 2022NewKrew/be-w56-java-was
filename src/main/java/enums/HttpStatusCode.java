package enums;

public enum HttpStatusCode {

    _200("200 OK"), _302("302 Found");

    private final String message;

    HttpStatusCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
