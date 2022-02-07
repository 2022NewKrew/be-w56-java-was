package http;

public enum ResponseStatus {
    SUCCESS(200), REDIRECT(302);

    private Integer code;
    ResponseStatus(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
