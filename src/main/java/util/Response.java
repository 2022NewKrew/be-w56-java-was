package util;

import lombok.Builder;
import lombok.Getter;

import static util.Constant.BLANK;
import static util.Constant.HTTP_1_1;

@Getter
public class Response {
    private final String version;
    private final String statusCode;
    private final String reasonPhrase;
    private final String contextType;
    private final byte[] body;
    private final Integer bodyLength;

    @Builder
    public Response(String statusCode, String reasonPhrase, String contextType, byte[] body) {
        this.version = HTTP_1_1;
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.contextType = contextType;
        this.body = body;
        this.bodyLength = body.length;
    }

    public String getStatusLine() {
        return version + BLANK + statusCode + BLANK + reasonPhrase;
    }
}
