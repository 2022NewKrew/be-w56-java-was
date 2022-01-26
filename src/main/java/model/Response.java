package model;

public class Response {
    private final String header;
    private final byte[] body;
    public Response(String header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }
}
