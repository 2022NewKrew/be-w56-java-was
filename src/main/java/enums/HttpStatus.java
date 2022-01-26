package enums;

public enum HttpStatus {
    OK("202 OK"),
    FOUND("302 Found");

    private String status;

    private HttpStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
