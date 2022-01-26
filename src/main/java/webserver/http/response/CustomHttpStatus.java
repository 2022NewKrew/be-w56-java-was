package webserver.http.response;

public enum CustomHttpStatus {
    NOT_FOUND(404),
    OK(200);

    int code;

    CustomHttpStatus(int code) {
        this.code = code;
    }
}
