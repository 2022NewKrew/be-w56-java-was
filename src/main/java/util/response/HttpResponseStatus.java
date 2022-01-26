package util.response;

public enum HttpResponseStatus {
    SUCCESS(200), REDIRECT(302), NOT_FOUND(404), INTERNAL_ERROR(500);

    private final int value;

    HttpResponseStatus(int value) {
        this.value = value;
    }
}
