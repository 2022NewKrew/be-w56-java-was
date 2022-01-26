package webserver.response;

public class HttpResponse {

    private HttpResponseLine responseLine;
    private HttpResponseHeader responseHeader;
    private HttpResponseBody responseBody;

    public HttpResponse(HttpResponseLine responseLine, HttpResponseHeader responseHeader, HttpResponseBody responseBody) {
        this.responseLine = responseLine;
        this.responseHeader = responseHeader;
        this.responseBody = responseBody;
    }

    public HttpResponseLine getResponseLine() {
        return responseLine;
    }

    public HttpResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public byte[] getBytesForBodyContent() {
        return responseBody.getBytes();
    }

    public int getLengthOfBodyContent() {
        return responseBody.getLengthOfBody();
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.responseLine.setStatusCode(httpStatus);
    }

    public void setHeaderIfAbsent(String key, String[] values) {
        responseHeader.putHeaderIfAbsent(key, values);
    }

    public void setResponseBody(byte[] content) {
        this.responseBody.setBody(content);
    }
}
