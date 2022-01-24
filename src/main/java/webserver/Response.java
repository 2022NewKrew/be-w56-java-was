package webserver;

public class Response {

    private final String statusCode;
    private final String statusText;
    private final byte[] body;

    public static Response from(String statusCode, String statusText, byte[] body) {
        return new Response(statusCode, statusText, body);
    }

    private Response(String statusCode, String statusText, byte[] body) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.body = body;
    }

    public String getHttpStatus() {
        return statusCode + " " + statusText;
    }

    public byte[] getBody() {
        return body;
    }

    public int getBodyLength() {
        return body.length;
    }
}
