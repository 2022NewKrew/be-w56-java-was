package dto;

public class ResponseBodyDto {

    private byte[] body;
    private String contentType;

    public ResponseBodyDto(byte[] body, String contentType) {
        this.body = body;
        this.contentType = contentType;
    }

    public byte[] getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }
}
