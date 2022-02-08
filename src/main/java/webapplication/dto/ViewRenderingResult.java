package webapplication.dto;

public class ViewRenderingResult {

    private final byte[] body;

    public ViewRenderingResult(byte[] body) {
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }
}
