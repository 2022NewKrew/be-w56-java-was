package network;

public class ResponseBody {

    private final String status;
    private final byte[] body;
    private String location;

    public ResponseBody(String status, byte[] body) {
        this.status = status;
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public byte[] getBody() {
        return body;
    }

    public String getLocation() {
        return location;
    }
}
