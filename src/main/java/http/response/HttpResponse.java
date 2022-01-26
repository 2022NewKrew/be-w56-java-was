package http.response;

import http.HttpMessage;
import http.header.HttpHeaders;
import lombok.Builder;
import lombok.Getter;

@Getter
public class HttpResponse extends HttpMessage {
    String status;

    @Builder
    public HttpResponse(String protocolVersion, HttpHeaders headers,
                        String status) {
        super(protocolVersion, headers);
        this.status = status;
    }
}
