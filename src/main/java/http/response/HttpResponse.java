package http.response;

import http.HttpMessage;
import http.header.HttpHeaders;
import http.header.HttpProtocolVersions;
import http.view.ViewResolver;
import lombok.Builder;
import lombok.Getter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

@Getter
public class HttpResponse extends HttpMessage {
    private final String status;
    private final byte[] body;

    @Builder
    public HttpResponse(String protocolVersion, HttpHeaders headers,
                        String status, String uri) {
        super(protocolVersion, headers);
        this.status = status;
        this.body = uri != null ? ViewResolver.getView(uri) : new byte[0];
    }

    public void send(DataOutputStream dos) throws IOException {
        dos.writeBytes(String.format("%s %s\r\n", protocolVersion, status));
        for (Map.Entry<String, String> header: headers) {
            dos.writeBytes(String.format("%s: %s\r\n", header.getKey(), header.getValue()));
        }

        if (body.length > 0) {
            dos.writeBytes(String.format("Content-Length: %d\r\n", body.length));
        }

        dos.writeBytes("\r\n");

        if (body.length > 0) {
            dos.write(body, 0, body.length);
        }

        dos.flush();
    }

    // --------------------------------------------------------------------------------
    // Factory methods
    // TODO: 재사용 가능하도록 리팩토링
    public static HttpResponse ok(String path) {
        return HttpResponse.builder()
                .protocolVersion(HttpProtocolVersions.HTTP_1_1.getValue())
                .headers(new HttpHeaders())
                .status("200 OK")
                .uri(path)
                .build();
    }

    public static HttpResponse found(String redirectLocation) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", redirectLocation);

        return HttpResponse.builder()
                .protocolVersion(HttpProtocolVersions.HTTP_1_1.getValue())
                .headers(headers)
                .status("302 Found")
                .build();
    }
}
