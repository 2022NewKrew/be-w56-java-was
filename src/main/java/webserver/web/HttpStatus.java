package webserver.web;

public enum HttpStatus {
    OK("200 OK"), NOT_FOUND("404 NOT FOUND"), REDIRECT("302 FOUND");

    private final String message;

    HttpStatus(String message) {
        this.message = message;
    }

    public String valueOf() {
        return message;
    }
}
