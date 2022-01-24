package util;

import lombok.Builder;

public class Response {

    private final String statusCode;
    private final String statusText;
    private final String contextType;
    private final byte[] body;

    @Builder
    public Response(String statusCode, String statusText, String contextType, byte[] body) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.contextType = contextType;
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

    public String getContextType() {
        return contextType;
    }
}
