package http.response;

public enum StatusCode {
    OK("200 OK");

    private final String status;

    StatusCode(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
