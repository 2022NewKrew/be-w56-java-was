package http;

import javax.annotation.Nullable;
import lombok.Builder;

public class Response {

    private final Status status;
    private final byte[] body;

    @Builder
    public Response(Status status, @Nullable byte[] body) {
        this.status = status;
        this.body = body == null ? new byte[0] : body;
    }

    public String getHttpStatus() {
        return status.name() + " " + status.getCode();
    }

    public byte[] getBody() {
        return body;
    }

    public int getBodyLength() {
        return body.length;
    }
}
