package webserver.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.DataOutputStream;

@Getter
@ToString
@EqualsAndHashCode
public class HttpResponse {
    private HttpResponseStatusLine httpResponseStatusLine;
    private HttpResponseHeader httpResponseHeader;
    private byte[] body;
    private DataOutputStream dos;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void setHttpResponseStatusLine(HttpResponseStatusLine httpResponseStatusLine) {
        this.httpResponseStatusLine = httpResponseStatusLine;
    }

    public void setHttpResponseHeader(HttpResponseHeader httpResponseHeader) {
        this.httpResponseHeader = httpResponseHeader;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(httpResponseStatusLine.getVersion() + " " + httpResponseStatusLine.getCode() + " " + httpResponseStatusLine.getMessage() + " \r\n");
        sb.append("Content-Type: " + httpResponseHeader.getContentType() + "\r\n");
        sb.append("Content-Length: " + body.length + "\r\n");
        sb.append("\r\n");

        return sb.toString();
    }
}
