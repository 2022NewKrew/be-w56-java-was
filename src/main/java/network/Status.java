package network;

public enum Status {
    OK(200), FOUND(302), NOTFOUND(404);

    private int value;
    Status(int value) {
        this.value = value;
    }
}
