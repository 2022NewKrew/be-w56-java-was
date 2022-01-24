package webserver.response;

public enum HttpStatus {

    OK(200),
    NOT_FOUND(404);

    private int num;

    HttpStatus(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
