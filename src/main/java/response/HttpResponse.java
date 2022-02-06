package response;

import java.util.HashMap;
import java.util.Map;
import response.ResponseHeader.ResponseHeaderBuilder;

public class HttpResponse {

    private final String responseStatusLine;
    private final ResponseHeader responseHeader;
    private final byte[] responseBody;

    private HttpResponse(HttpResponseBuilder builder) {
        responseStatusLine = String.format("%s %s", builder.protocol, builder.statusCode);
        responseHeader = builder.header;
        responseBody = builder.body;
    }

    public static Map<String, String> defaultHeader(Integer bodyLength) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "text/html;charset=utf-8");
        header.put("Content-Length", bodyLength.toString());
        return header;
    }

    public String getResponseStatusLine() {
        return responseStatusLine;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public static class HttpResponseBuilder {

        private final HttpStatusCode statusCode;
        private String protocol = "HTTP/1.1";
        private ResponseHeader header = new ResponseHeaderBuilder().build();
        private byte[] body = new byte[0];

        public HttpResponseBuilder(HttpStatusCode statusCode) {
            this.statusCode = statusCode;
        }

        public HttpResponseBuilder setProtocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public HttpResponseBuilder setHeader(ResponseHeader header) {
            this.header = header;
            return this;
        }

        public HttpResponseBuilder setBody(byte[] body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }
}
