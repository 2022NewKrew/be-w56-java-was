package infrastructure.model;

import java.util.Objects;

public class HttpResponse {

    private final ResponseLine responseLine;
    private final HttpHeader httpHeader;
    private HttpBody responseBody;

    public HttpResponse(ResponseLine responseLine, HttpHeader httpHeader) {
        this.responseLine = responseLine;
        this.httpHeader = httpHeader;
    }

    public HttpResponse(ResponseLine responseLine, HttpHeader httpHeader, HttpBody responseBody) {
        this.responseLine = responseLine;
        this.httpHeader = httpHeader;
        httpHeader.addHeader(Pair.of("Content-Length", String.valueOf(responseBody.length())));
        this.responseBody = responseBody;
    }

    public ResponseLine getResponseLine() {
        return responseLine;
    }

    public HttpHeader getHttpHeader() {
        return httpHeader;
    }

    public HttpBody getResponseBody() {
        return responseBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpResponse that = (HttpResponse) o;
        return Objects.equals(responseLine, that.responseLine) && Objects.equals(httpHeader, that.httpHeader) && Objects.equals(responseBody, that.responseBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseLine, httpHeader, responseBody);
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "responseLine=" + responseLine +
                ", httpHeader=" + httpHeader +
                ", responseBody=" + responseBody +
                '}';
    }
}
