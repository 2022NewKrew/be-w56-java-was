package webserver.response;

public enum HttpStatus {

    OK(200),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private int num;

    HttpStatus(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
