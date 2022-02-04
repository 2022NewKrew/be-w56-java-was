package http;

public enum  HttpStatusCode {
    OK("200 OK"),
    REDIRECT("302 Found");

    private final String code;

    HttpStatusCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
