package network;

public class ResponseBody {

    private final HttpStatus httpStatus;
    private final byte[] body;
    private String location;

    public ResponseBody(HttpStatus httpStatus, byte[] body) {
        this.httpStatus = httpStatus;
        this.body = body;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public byte[] getBody() {
        return body;
    }

    public String getLocation() {
        return location;
    }
}
