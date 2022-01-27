package util;

public enum ResponseStatus {
    OK(200),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    FOUND(302);

    private final int value;

    ResponseStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
